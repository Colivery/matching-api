package com.colivery.engine.model

data class SearchResponse(val orderIds: List<SearchResult>)

data class SearchResult(val orderId: String, val distanceKm: Double, val activitySequence: List<Activity>)

enum class ActivityType {
    navigate, drop_off, pickup
}

data class Activity(val coordinate: Coordinate, val type: ActivityType, val shopName: String?, val pickupAddress: String?)