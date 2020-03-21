package com.colivery.engine.service

import com.colivery.engine.model.Order
import com.google.cloud.firestore.GeoPoint
import org.springframework.stereotype.Service

enum class PoIType {
    Supermarket, Pharmacy, grocery
}

data class PoI(val type: PoIType, val position: GeoPoint)

@Service
class PoIService {
    fun findPoIs(position: GeoPoint, radiusKm: Float): Array<PoI> {
        return arrayOf();
    }

    fun extractPoIs(orders: Array<Order>): Array<PoI> {
        return orders
                .filter { order -> order.pickupLocation != null }
                .map { order -> PoI(order.shopType, order.pickupLocation as GeoPoint) }
                .toTypedArray();
    }
}