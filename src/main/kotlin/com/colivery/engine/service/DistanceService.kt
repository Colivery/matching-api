package com.colivery.engine.service

import com.colivery.engine.model.Activity
import com.colivery.geo.Coordinate
import com.colivery.geo.Distance
import org.springframework.stereotype.Service

private const val earthRadiusKm: Double = 6372.8

@Service
class DistanceService {

    fun calculateTotalDistance(activitySequence: List<Activity>): Double {
        var totalDistance = 0.0
        var lastCoordinate: Coordinate? = null
        for (activity in activitySequence) {
            if (lastCoordinate == null) {
                lastCoordinate = activity.coordinate
                continue
            }
            totalDistance += Distance.haversine(lastCoordinate, activity.coordinate)
            lastCoordinate = activity.coordinate
        }
        return totalDistance
    }

    fun calculateRange(orders: List<Order>, startLocation: Coordinate, minRange: Double = 5.0): Float {
        val ne = Coordinate(orders.map { order -> order.dropOffLocation.latitude }.min()!!,
                orders.map { order -> order.dropOffLocation.longitude }.min()!!)

        val sw = Coordinate(orders.map { order -> order.dropOffLocation.latitude }.max()!!,
                orders.map { order -> order.dropOffLocation.longitude }.max()!!)

        return maxOf(haversine(startLocation, ne),
                haversine(startLocation, sw),
                minRange).toFloat()
    }
}