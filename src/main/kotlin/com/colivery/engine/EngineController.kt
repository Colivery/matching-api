package com.colivery.engine

import com.colivery.engine.model.SearchRequest
import com.colivery.engine.model.SearchResult
import com.colivery.engine.service.DistanceService
import com.colivery.engine.service.PoIService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class EngineController {
    @Autowired
    lateinit var poiService: PoIService

    @Autowired
    lateinit var distanceService: DistanceService

    @Autowired
    lateinit var fireStoreService: FireStoreService

    @PostMapping("/search")
    fun search(@RequestBody request: SearchRequest): SearchResult? {

        val position = request.position
        val radius = request.radiusKm

        val orderIdsSorted =  fireStoreService.getAllOrdersWithStateToBeDelivered()
                .map { order ->
                    Pair(order.pickupLocation?.let { distanceService.calculateDistance(position, it) }, order)
                }
                .filter { pair -> pair.first!! < radius }
                .sortedBy { pair -> pair.first }
                .map { pair -> pair.second }
                .map { order -> order.id }
                .toList()

        return SearchResult(orderIdsSorted)
    }
}