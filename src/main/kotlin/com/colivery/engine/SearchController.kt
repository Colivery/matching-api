package com.colivery.engine

import com.colivery.engine.model.*
import com.colivery.engine.service.DistanceService
import com.colivery.engine.service.OrderService
import com.colivery.engine.service.PoIService
import com.colivery.engine.service.RouteService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/search")
class SearchController {

    @Autowired
    lateinit var poiService: PoIService

    @Autowired
    lateinit var distanceService: DistanceService

    @Autowired
    lateinit var orderService: OrderService

    @Autowired
    lateinit var routeService: RouteService

    @PostMapping("/route")
    fun route(@RequestBody @Valid request: RouteRequest): RouteResponse {
        val orders = orderService.fetchOrdersByIds(request.orderIds)
        val allPoIs = poiService.findAllPoIs(request.coordinate, request.range, orders)
        val activitySequence = routeService.buildRoute(request.coordinate, orders, allPoIs)

        return RouteResponse(distanceService.calculateTotalDistance(activitySequence),
                activitySequence,
                buildMapsLink(activitySequence))
    }

    @PostMapping("/order")
    fun query(@RequestBody @Valid request: OrderRequest): OrderResponse {
        val startLocation = request.coordinate
        val radius = request.range

        val orders = orderService.fetchAllValidOrders(startLocation, radius)

        val allPoIs = poiService.findAllPoIs(startLocation, radius, orders)

        val resultList = orders
                .asSequence()
                .map { order ->
                    buildSearchResult(startLocation, order, allPoIs)
                }
                .sortedBy { result -> result.distanceKm }
                .toList()

        return OrderResponse(resultList, allPoIs)
    }

    fun buildSearchResult(startLocation: Coordinate, order: Order, allPoIs: Set<PoI>): OrderResult {

        val firstActivity = Activity(null, startLocation, ActivityType.start, null, null, null)
        val pickupLocation: Coordinate
        val shopName: String
        val shopAddress: String
        val pickup: Activity

        if (order.pickupLocation == null) {
            val poi = poiService.findBestPoI(startLocation, order.dropOffLocation, order.shopType, allPoIs)
            shopName = poi.name
            shopAddress = poi.address
            pickupLocation = poi.coordinate
            pickup = Activity(order.id, pickupLocation, ActivityType.pickup, shopName, shopAddress, false)
        } else {
            shopName = order.shopName ?: ""
            shopAddress = order.pickupAddress ?: ""
            pickupLocation = order.pickupLocation
            pickup = Activity(order.id, pickupLocation, ActivityType.pickup, shopName, shopAddress, true)
        }

        val dropOff = Activity(order.id, order.dropOffLocation, ActivityType.drop_off, null, null, null)
        val activitySequence = listOf(firstActivity, pickup, dropOff)
        val distance = distanceService.calculateTotalDistance(activitySequence)
        return OrderResult(order.id,
                distance,
                activitySequence,
                buildMapsLink(activitySequence))
    }

    private fun buildMapsLink(activitySequence: List<Activity>): String {
        val mapsLink = StringBuilder()
        mapsLink.append("https://www.google.com/maps/dir")
        var lastCoordinate: Coordinate? = null
        for (activity in activitySequence) {
            if (lastCoordinate == null) {
                mapsLink.append("/" + activity.coordinate.latitude + "," + activity.coordinate.longitude)
                lastCoordinate = activity.coordinate
            } else if (lastCoordinate != activity.coordinate) {
                mapsLink.append("/" + activity.coordinate.latitude + "," + activity.coordinate.longitude)
            }
        }
        return mapsLink.toString()
    }

}