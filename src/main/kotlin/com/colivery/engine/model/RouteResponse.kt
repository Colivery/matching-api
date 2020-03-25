package com.colivery.engine.model

data class RouteResponse(
        val distanceKm: Double,
        val activitySequence: List<Activity>,
        val mapsLink: String
)