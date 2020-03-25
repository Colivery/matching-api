package com.colivery.engine

import com.colivery.engine.model.*
import com.google.cloud.firestore.DocumentSnapshot
import com.google.cloud.firestore.GeoPoint

@Suppress("unchecked_cast")
fun DocumentSnapshot.toOrder() = Order(
        id = id,
        userId = notNull("user_id", this::getString),
        pickupAddress = getString("pickup_address"),
        pickupLocation = getGeoPoint("pickup_location")?.toCoordinate(),
        dropOffLocation = notNull("dropoff_location", this::getGeoPoint).toCoordinate(),
        shopName = getString("shop_name"),
        shopType = PoIType.valueOf(notNull("shop_type", this::getString))
)

fun GeoPoint.toCoordinate() = Coordinate(
        latitude = latitude,
        longitude = longitude)

private inline fun <reified T : Any> notNull(fieldName: String, provider: (fieldName: String) -> T?): T {
    val value: T? = provider(fieldName)
    return requireNotNull(value) { "$fieldName must not be null" }
}
