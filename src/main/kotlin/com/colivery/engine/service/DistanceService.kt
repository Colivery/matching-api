package com.colivery.engine.service

import com.colivery.engine.model.Coordinate
import org.springframework.stereotype.Service

typealias Degree = Double
typealias Radian = Double

fun Degree.toRadian(): Radian = this / 180 * Math.PI

@Service
class DistanceService {
    fun calculateDistance(position: Coordinate, position2: Coordinate): Double {
        var earthRadiusKm = 6371;

        var dLat = (Math.abs(position2.latitude - position.latitude)).toRadian();
        var dLon = (Math.abs(position2.longitude - position.longitude)).toRadian();

        var lat1 = position.latitude.toRadian();
        var lat2 = position2.latitude.toRadian();

        var a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
        var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadiusKm * c;
    }
}