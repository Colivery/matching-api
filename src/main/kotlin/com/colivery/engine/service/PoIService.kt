package com.colivery.engine.service

import com.colivery.engine.model.Coordinate
import com.colivery.engine.model.Order
import com.colivery.engine.model.PoI
import com.colivery.engine.model.PoIType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PoIService {

    @Autowired
    lateinit var distanceService: DistanceService

    fun findBestPoI(start: Coordinate, dropOff: Coordinate, type: PoIType, allPoIs: List<PoI>): PoI {
        var pois = allPoIs.filter { poi -> poi.type == type }

        val minBy = pois
                .minBy { poi ->
                    distanceService.calculateDistance(start, poi.coordinate) + distanceService.calculateDistance(poi.coordinate, dropOff)
                }
        return minBy as PoI
    }

    fun extractPoIs(orders: List<Order>): List<PoI> {
        return orders
                .filter { order -> order.pickupLocation != null }
                .map { order ->
                    PoI(order.shopType, order.pickupLocation as Coordinate,
                            order.pickupAddress ?: "", order.shopName ?: "")
                }
    }
}