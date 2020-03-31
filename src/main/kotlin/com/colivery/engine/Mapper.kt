package com.colivery.engine

import com.colivery.engine.model.Coordinate
import com.colivery.engine.model.Order
import com.colivery.engine.model.OrderItem
import com.colivery.engine.model.PoIType
import com.google.cloud.firestore.DocumentSnapshot
import com.google.cloud.firestore.GeoPoint

@Suppress("unchecked_cast")
fun DocumentSnapshot.toOrder(items: List<OrderItem>?) = Order(
        id = id,
        userId = notNull("user_id", this::getString),
        pickupAddress = getString("pickup_address"),
        pickupLocation = getGeoPoint("pickup_location")?.toCoordinate(),
        dropOffLocation = notNull("dropoff_location", this::getGeoPoint).toCoordinate(),
        shopName = getString("shop_name"),
        shopType = PoIType.valueOf(notNull("shop_type", this::getString)),
        hint = getString("hint"),
        driverUserId = getString("driver_user_id"),
        status = notNull("status", this::getString),
        items = items
)

@Suppress("unchecked_cast")
fun DocumentSnapshot.toOrderItem() = OrderItem(
        id = id,
        status = notNull("status", this::getString),
        description = notNull("description", this::getString)
)

fun GeoPoint.toCoordinate() = Coordinate(
        latitude = latitude,
        longitude = longitude)

private inline fun <reified T : Any> notNull(fieldName: String, provider: (fieldName: String) -> T?): T {
    val value: T? = provider(fieldName)
    return requireNotNull(value) { "$fieldName must not be null" }
}
