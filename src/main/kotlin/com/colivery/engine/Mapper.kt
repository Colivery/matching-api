package com.colivery.engine

import com.colivery.engine.model.Order
import com.colivery.engine.model.OrderItem
import com.colivery.engine.model.PoIType
import com.colivery.geo.Coordinate
import com.colivery.geo.GeoHash
import com.google.cloud.firestore.DocumentSnapshot
import com.google.cloud.firestore.GeoPoint
import java.time.Instant

@Suppress("unchecked_cast")
fun DocumentSnapshot.toOrder(items: List<OrderItem>?) = Order(
        id = id,
        created = getCreated(),
        updated = getUpdated(),
        userId = notNull("user_id", this::getString),
        pickupLocationGeohash = getString("pickup_location_geohash"),
        pickupAddress = getString("pickup_address"),
        pickupLocation = getGeoPoint("pickup_location")?.toCoordinate(),
        dropoffLocation = notNull("dropoff_location", this::getGeoPoint).toCoordinate(),
        dropoffLocationGeohash = notNull("dropoff_location_geohash", this::getString),
        shopName = getString("shop_name"),
        shopType = PoIType.valueOf(notNull("shop_type", this::getString)),
        hint = getString("hint"),
        driverUserId = getString("driver_user_id"),
        status = notNull("status", this::getString),
        items = items,
        maxPrice = getLong("max_price")
)

fun Order.pseudonymized() = Order(
        id = id,
        created = created,
        updated = updated,
        userId = userId,
        pickupLocationGeohash = pickupLocationGeohash,
        pickupAddress = pickupAddress,
        pickupLocation = pickupLocation,
        dropoffLocationGeohash = dropoffLocationGeohash,
        dropoffLocation = GeoHash.decode(dropoffLocationGeohash),
        shopName = shopName,
        shopType = shopType,
        hint = hint,
        driverUserId = driverUserId,
        status = status,
        items = items,
        maxPrice = maxPrice
)

private fun DocumentSnapshot.getUpdated(): Instant? = updateTime?.seconds?.let { Instant.ofEpochSecond(it) }
private fun DocumentSnapshot.getCreated(): Instant? = createTime?.seconds?.let { Instant.ofEpochSecond(it) }

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
