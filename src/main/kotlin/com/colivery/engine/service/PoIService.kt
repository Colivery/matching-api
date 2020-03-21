package com.colivery.engine.service

import com.colivery.engine.model.GeoPosition
import com.colivery.engine.model.Order
import org.springframework.stereotype.Service

enum class PoIType {
    Supermarket, Pharmacy
}

data class PoI(val type: PoIType, val position: GeoPosition)

@Service
class PoIService {
    fun findPoIs(position: GeoPosition, radiusKm: Float): Array<PoI> {
        return arrayOf();
    }

    fun extractPoIs(orders: Array<Order>): Array<PoI> {
        return orders
                .filter { order -> order.pickupLocation != null }
                .map { order -> PoI(order.shopType, order.pickupLocation as GeoPosition) }
                .toTypedArray();
    }
}