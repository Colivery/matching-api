package com.colivery.engine

import com.colivery.engine.model.Activity
import com.colivery.engine.model.ActivityType
import com.colivery.engine.model.Coordinate
import com.colivery.engine.model.Order
import com.colivery.engine.model.PoI
import com.colivery.engine.model.SearchRequest
import com.colivery.engine.model.SearchResponse
import com.colivery.engine.model.SearchResult
import com.colivery.engine.service.DistanceService
import com.colivery.engine.service.FireStoreService
import com.colivery.engine.service.PoIService
import com.colivery.engine.service.poi.PoiSearchService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
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
    lateinit var poiSearchService: PoiSearchService

    @Autowired
    lateinit var distanceService: DistanceService

    @Autowired
    lateinit var fireStoreService: FireStoreService

    @PostMapping("/query")
    fun search(@RequestBody @Valid request: SearchRequest): SearchResponse? {
        logger.info("POST /search/query $request")

        val startLocation = request.coordinate
        val radius = request.range

        val orders = fireStoreService.getAllOrdersWithStateToBeDelivered()
                .filter { order -> distanceService.calculateDistance(startLocation, order.dropOffLocation) <= radius }
        orders.forEach { it.fixType() }

        val orderPoIs = poiService.extractPoIs(orders)

        val orderPoITypes = orders.map { order -> order.shopType }.toSet()
        val allPoIs = poiSearchService.findPoIs(startLocation, radius, orderPoITypes).toMutableList()
        allPoIs.addAll(orderPoIs)

        val resultList = orders
                .asSequence()
                .map { order ->
                    buildSearchResult(startLocation, order, allPoIs)
                }
                .sortedBy { result -> result.distanceKm }
                .toList()

        return SearchResponse(resultList, allPoIs)
    }

    fun buildSearchResult(startLocation: Coordinate, order: Order, allPoIs: List<PoI>): SearchResult {

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

        val distance = distanceService.calculateDistance(startLocation, pickupLocation)
        val dropOff = Activity(order.dropOffLocation, ActivityType.drop_off, null, null, null)
        val activitySequence: List<Activity> = listOf(firstActivity, pickup, dropOff)
        return SearchResult(order.id, distance, activitySequence)
    }

}