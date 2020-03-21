package com.colivery.engine.service

import com.colivery.engine.model.GeoPosition
import org.springframework.stereotype.Service

typealias Degree = Double
typealias Radian = Double

fun Degree.toRadian(): Radian = this / 180 * Math.PI

@Service
class DistanceService {
    fun calculateDistance(position: GeoPosition, position2: GeoPosition): Double {
        var earthRadiusKm = 6371;

        var dLat = (position2.lat-position.lat).toRadian();
        var dLon = (position2.lon-position.lon).toRadian();

        var lat1 = position.lat.toRadian();
        var lat2 = position2.lat.toRadian();

        var a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(lat1) * Math.cos(lat2);
        var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return earthRadiusKm * c;
    }
}