package com.colivery.engine.model

data class OrderResponse(val orders: List<OrderResult>, val pois: Set<PoI>)

data class OrderResult(val orderId: String,
                       val distanceKm: Double,
                       val activitySequence: List<Activity>,
                       val mapsLink: String)

enum class ActivityType {
    start, drop_off, pickup
}

data class Activity(val orderId: String?,
                    val coordinate: Coordinate,
                    val type: ActivityType,
                    val shopName: String?,
                    val pickupAddress: String?,
                    val orderRestricted: Boolean?)