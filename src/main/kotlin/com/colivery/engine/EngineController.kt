package com.colivery.engine

import com.colivery.engine.model.*
import com.colivery.engine.service.DistanceService
import com.colivery.engine.service.PoI
import com.colivery.engine.service.PoIService
import com.colivery.engine.service.poi.PoiSearchService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class EngineController {
    @Autowired
    lateinit var poiService: PoIService

    @Autowired
    lateinit var poiSearchService: PoiSearchService

    @GetMapping("/")
    fun get(): Array<PoI> {
        return poiSearchService.findPoIs(Coordinate(48.16058943132621, 11.565932035446167), 2.0F)
    }

    @Autowired
    lateinit var distanceService: DistanceService

    @Autowired
    lateinit var fireStoreService: FireStoreService

    @PostMapping("/search")
    fun search(@RequestBody request: SearchRequest): SearchResponse? {

        val startLocation = request.coordinate
        val radius = request.range

        val orders = fireStoreService.getAllOrdersWithStateToBeDelivered()

        val allPoIs = poiSearchService.findPoIs(startLocation, radius).toMutableList()
        allPoIs.addAll(poiService.extractPoIs(orders))

        val resultList = orders
                .asSequence()
                .filter { order -> distanceService.calculateDistance(startLocation, order.dropOffLocation) <= radius }
                .map { order ->
                    buildSearchResult(startLocation, order, allPoIs)
                }
                .sortedBy { result -> result.distanceKm }
                .toList()

        return SearchResponse(resultList)
    }

    fun buildSearchResult(startLocation: Coordinate, order: Order, allPoIs: List<PoI>): SearchResult {

        val firstActivity = Activity(startLocation, ActivityType.navigate, null, null)
        val pickupLocation: Coordinate
        val shopName: String
        val shopAddress: String

        if (order.pickupLocation == null) {
            val poi = poiService.findBestPoI(startLocation, order.dropOffLocation, order.shopType, allPoIs)
            shopName = poi.name
            shopAddress = poi.address
            pickupLocation = poi.location
        } else {
            shopName = order.shopName ?: ""
            shopAddress = order.pickupAddress ?: ""
            pickupLocation = order.pickupLocation
        }

        val distance = distanceService.calculateDistance(startLocation, pickupLocation)
        val pickup = Activity(pickupLocation, ActivityType.pickup, shopName, shopAddress)
        val dropOff = Activity(order.dropOffLocation, ActivityType.drop_off, null, null)
        val activitySequence: List<Activity> = listOf(firstActivity, pickup, dropOff)
        return SearchResult(order.id, distance, activitySequence)
    }

}