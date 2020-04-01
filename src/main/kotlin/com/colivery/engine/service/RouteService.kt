package com.colivery.engine.service

import com.colivery.engine.model.*
import com.colivery.geo.Coordinate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class RouteService {

    @Autowired
    lateinit var distanceService: DistanceService

    fun buildRoute(startLocation: Coordinate, orders: List<Order>, allPoIs: Set<PoI>): List<Activity> {
        var routes: Set<List<Activity>> = setOf(mutableListOf(Activity(null, startLocation, ActivityType.start, null, null, null)))
        for (order in orders) {
            routes = addOrder(routes,
                    order,
                    allPoIs)
        }
        return routes.minBy { route -> distanceService.calculateTotalDistance(route) }.orEmpty()
    }

    private fun addOrder(routes: Set<List<Activity>>, order: Order, allPoIs: Set<PoI>): MutableSet<List<Activity>> {
        val resultRoutes: MutableSet<List<Activity>> = mutableSetOf()
        val dropOff = order.buildDropOff()

        for (route in routes) {
            for (pickupIndex in 1..route.size) {
                if (order.pickupLocation != null) {
                    val routeBuffer = route.toMutableList()
                    routeBuffer.add(pickupIndex, order.buildPickup())
                    for (dropOffIndex in (pickupIndex + 1)..(routeBuffer.size)) {
                        routeBuffer.add(dropOffIndex, dropOff)
                        resultRoutes.add(routeBuffer.toList())
                        routeBuffer.removeAt(dropOffIndex)
                    }
                } else {
                    val orderPois = allPoIs.filter { poi -> poi.type == order.shopType }
                    for (poi in orderPois) {
                        val routeBuffer = route.toMutableList()
                        routeBuffer.add(pickupIndex, order.buildPickup(poi))
                        for (dropOffIndex in (pickupIndex + 1)..(routeBuffer.size)) {
                            routeBuffer.add(dropOffIndex, dropOff)
                            resultRoutes.add(routeBuffer.toList())
                            routeBuffer.removeAt(dropOffIndex)
                        }
                    }
                }
            }
        }
        return resultRoutes
    }
}
