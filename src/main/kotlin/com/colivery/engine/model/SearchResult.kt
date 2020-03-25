package com.colivery.engine.model

data class SearchResponse(val orders: List<SearchResult>, val pois: Set<PoI>)

data class SearchResult(val orderId: String,
                        val distanceKm: Double,
                        val activitySequence: List<Activity>,
                        val mapsLink: String)

enum class ActivityType {
    start, drop_off, pickup
}

data class Activity(val coordinate: Coordinate,
                    val type: ActivityType,
                    val shopName: String?,
                    val pickupAddress: String?,
                    val orderRestricted: Boolean?)