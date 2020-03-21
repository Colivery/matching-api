package com.colivery.engine

import com.colivery.engine.model.*
import com.colivery.engine.service.DistanceService
import com.colivery.engine.service.PoI
import com.colivery.engine.service.PoIService
import com.colivery.engine.service.poi.PoiSearchService
import com.google.cloud.firestore.GeoPoint
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
        return poiSearchService.findPoIs(GeoPoint(48.16058943132621,11.565932035446167), 2.0F)
    }

    @Autowired
    lateinit var distanceService: DistanceService

    @Autowired
    lateinit var fireStoreService: FireStoreService

    @PostMapping("/search")
    fun search(@RequestBody request: SearchRequest): SearchResponse? {

        val startLocation = request.position
        val radius = request.radiusKm

        val resultList = fireStoreService.getAllOrdersWithStateToBeDelivered()
                .asSequence()
                .filter { order -> distanceService.calculateDistance(startLocation, order.dropOffLocation) <= radius }
                .map { order ->
                    buildSearchResult(startLocation, radius, order)
                }
                .filter { result -> result.distanceKm <= radius }
                .sortedBy { result -> result.distanceKm }
                .toList()

        return SearchResponse(resultList)
    }

    fun buildSearchResult(startLocation: Coordinate, radius: Float, order: Order): SearchResult {

        val firstActivity = Activity(startLocation, ActivityType.navigate, null, null)
        val pickupLocation: Coordinate
        val shopName: String
        val shopAddress: String

        if (order.pickupLocation == null) {
            val poi = poiService.findPoINearby(firstActivity.location, radius, order.shopType)
            shopName = poi.name
            shopAddress = poi.address
            pickupLocation = poi.location
        } else {
            shopName = order.shopName!!
            shopAddress = order.shopAddress!!
            pickupLocation = order.pickupLocation!!
        }

        val distance = distanceService.calculateDistance(startLocation, pickupLocation)
        val pickup = Activity(pickupLocation, ActivityType.pickup, shopName, shopAddress)
        val dropOff = Activity(order.dropOffLocation, ActivityType.drop_off, null, null)
        val activitySequence: List<Activity> = listOf(firstActivity, pickup, dropOff)
        return SearchResult(order.id, distance, activitySequence)
    }

}