package com.colivery.engine.model

data class SearchResponse(val orders: List<SearchResult>, val pois: List<PoI>)

data class SearchResult(val orderId: String, val distanceKm: Double, val activitySequence: List<Activity>)

enum class ActivityType {
    start, drop_off, pickup
}

data class Activity(val coordinate: Coordinate,
                    val type: ActivityType,
                    val shopName: String?,
                    val pickupAddress: String?,
                    val orderRestricted: Boolean?)