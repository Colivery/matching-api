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
}