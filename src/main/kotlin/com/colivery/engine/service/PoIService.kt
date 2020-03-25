package com.colivery.engine.service

import com.colivery.engine.model.Coordinate
import com.colivery.engine.model.Order
import com.colivery.engine.model.PoI
import com.colivery.engine.model.PoIType
import com.colivery.engine.service.poi.PoiSearchService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PoIService {

    @Autowired
    lateinit var distanceService: DistanceService

    @Autowired
    lateinit var poiSearchService: PoiSearchService

    fun findBestPoI(start: Coordinate, dropOff: Coordinate, type: PoIType, allPoIs: Set<PoI>): PoI {
        var pois = allPoIs.filter { poi -> poi.type == type }

        val minBy = pois
                .minBy { poi ->
                    distanceService.haversine(start, poi.coordinate) + distanceService.haversine(poi.coordinate, dropOff)
                }
        return minBy as PoI
    }

    private fun extractPoIs(orders: List<Order>): Set<PoI> {
        return orders
                .filter { order -> order.pickupLocation != null }
                .map { order ->
                    PoI(order.shopType, order.pickupLocation as Coordinate,
                            order.pickupAddress ?: "", order.shopName ?: "")
                }.toSet()
    }

    fun findAllPoIs(startLocation: Coordinate, radius: Float, orders: List<Order>): Set<PoI> {
        val orderPoIs = extractPoIs(orders)
        val orderPoITypes = orders.map { order -> order.shopType }.toSet()
        val allPoIs = poiSearchService.findPoIs(startLocation, radius, orderPoITypes).toMutableSet()
        allPoIs.addAll(orderPoIs)
        return allPoIs
    }
}