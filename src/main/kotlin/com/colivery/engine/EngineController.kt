package com.colivery.engine

import com.colivery.engine.model.*
import com.colivery.engine.service.DistanceService
import com.colivery.engine.service.OrderService
import com.colivery.engine.service.PoIService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/search")
class EngineController {

    private val logger: Logger = LoggerFactory.getLogger(EngineController::class.java)

    @Autowired
    lateinit var poiService: PoIService

    @Autowired
    lateinit var distanceService: DistanceService

    @Autowired
    lateinit var orderService: OrderService

    @PostMapping("/query")
    fun search(@RequestBody @Valid request: SearchRequest): SearchResponse? {
        logger.info("POST /search/query $request")

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

        return SearchResponse(resultList, allPoIs)
    }

    fun buildSearchResult(startLocation: Coordinate, order: Order, allPoIs: Set<PoI>): SearchResult {

        val firstActivity = Activity(startLocation, ActivityType.start, null, null, null)
        val pickupLocation: Coordinate
        val shopName: String
        val shopAddress: String
        val pickup: Activity

        if (order.pickupLocation == null) {
            val poi = poiService.findBestPoI(startLocation, order.dropOffLocation, order.shopType, allPoIs)
            shopName = poi.name
            shopAddress = poi.address
            pickupLocation = poi.coordinate
            pickup = Activity(pickupLocation, ActivityType.pickup, shopName, shopAddress, false)
        } else {
            shopName = order.shopName ?: ""
            shopAddress = order.pickupAddress ?: ""
            pickupLocation = order.pickupLocation
            pickup = Activity(pickupLocation, ActivityType.pickup, shopName, shopAddress, true)
        }

        val distance = distanceService.haversine(startLocation, pickupLocation) +
                distanceService.haversine(pickupLocation, order.dropOffLocation)
        val dropOff = Activity(order.dropOffLocation, ActivityType.drop_off, null, null, null)
        val activitySequence: List<Activity> = listOf(firstActivity, pickup, dropOff)
        return SearchResult(order.id,
                distance,
                activitySequence,
                buildMapsLink(activitySequence))
    }

    private fun buildMapsLink(activitySequence: List<Activity>): String {
        val mapsLink = StringBuilder()
        mapsLink.append("https://www.google.com/maps/dir")
        for (activity in activitySequence) {
            mapsLink.append("/" + activity.coordinate.latitude + "," + activity.coordinate.longitude)
        }
        return mapsLink.toString()
    }

}