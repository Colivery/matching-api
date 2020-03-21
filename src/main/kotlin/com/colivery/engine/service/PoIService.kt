package com.colivery.engine.service

import com.colivery.engine.model.Coordinate
import com.colivery.engine.model.Order
import com.google.cloud.firestore.GeoPoint
import org.springframework.stereotype.Service

enum class PoIType {
    Supermarket, Pharmacy, grocery
}

data class PoI(val type: PoIType, val location: Coordinate, val address: String, val name: String)

@Service
class PoIService {
    fun findPoINearby(startLocation: Coordinate, radiusKm: Float, type: PoIType): PoI {
        return PoI(PoIType.Pharmacy, Coordinate(0.0, 0.0), "", "null");
    }

    fun extractPoIs(orders: Array<Order>): Array<PoI> {
        return orders
                .filter { order -> order.pickupLocation != null }
                .map { order -> PoI(order.shopType, order.pickupLocation as Coordinate, "", "") }
                .toTypedArray();
    }
}