package com.colivery.engine.service

import com.colivery.engine.model.Activity
import com.colivery.engine.model.Coordinate
import com.colivery.engine.model.Order
import com.colivery.engine.model.PoI
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class RouteService {

    @Autowired
    lateinit var distanceService: DistanceService

    fun buildRoute(startLocation: Coordinate, orders: List<Order>, allPoIs: Set<PoI>): List<Activity> {
        return emptyList()
    }
}
