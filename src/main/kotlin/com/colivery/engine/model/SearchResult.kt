package com.colivery.engine.model

import com.colivery.engine.service.Degree
import com.colivery.engine.service.PoIType

data class SearchResult(val orders: Array<Order>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SearchResult

        if (!orders.contentEquals(other.orders)) return false

        return true
    }

    override fun hashCode(): Int {
        return orders.contentHashCode()
    }
}

enum class OrderStatus {
    to_be_delivered, accepted, delivered, consumer_canceled
}

data class GeoPosition(val lat: Degree, val lon: Degree)

data class Order(val id: String,
                 val userId: String,
                 val driverUserId: String?,
                 val shopName: String?,
                 val shopType: PoIType,
                 val pickupAddress: String?,
                 val pickupLocation: GeoPosition?,
                 val products: Array<OrderItem>,
                 val status: OrderStatus,
                 val hint: String?,
                 val created: String,
                 val updated: String) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Order

        if (id != other.id) return false
        if (userId != other.userId) return false
        if (driverUserId != other.driverUserId) return false
        if (shopName != other.shopName) return false
        if (shopType != other.shopType) return false
        if (pickupAddress != other.pickupAddress) return false
        if (pickupLocation != other.pickupLocation) return false
        if (!products.contentEquals(other.products)) return false
        if (status != other.status) return false
        if (hint != other.hint) return false
        if (created != other.created) return false
        if (updated != other.updated) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + userId.hashCode()
        result = 31 * result + driverUserId.hashCode()
        result = 31 * result + shopName.hashCode()
        result = 31 * result + shopType.hashCode()
        result = 31 * result + pickupAddress.hashCode()
        result = 31 * result + pickupLocation.hashCode()
        result = 31 * result + products.contentHashCode()
        result = 31 * result + status.hashCode()
        result = 31 * result + hint.hashCode()
        result = 31 * result + created.hashCode()
        result = 31 * result + updated.hashCode()
        return result
    }

}


enum class OrderItemStatus {
    todo, done, na
}

data class OrderItem(val id: String,
                     val description: String,
                     val status: OrderItemStatus)
