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
                 val user_id: String,
                 val driver_user_id: String?,
                 val shop_name: String?,
                 val shop_type: PoIType,
                 val pickup_address: String?,
                 val pickup_location: GeoPosition?,
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
        if (user_id != other.user_id) return false
        if (driver_user_id != other.driver_user_id) return false
        if (shop_name != other.shop_name) return false
        if (shop_type != other.shop_type) return false
        if (pickup_address != other.pickup_address) return false
        if (pickup_location != other.pickup_location) return false
        if (!products.contentEquals(other.products)) return false
        if (status != other.status) return false
        if (hint != other.hint) return false
        if (created != other.created) return false
        if (updated != other.updated) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + user_id.hashCode()
        result = 31 * result + driver_user_id.hashCode()
        result = 31 * result + shop_name.hashCode()
        result = 31 * result + shop_type.hashCode()
        result = 31 * result + pickup_address.hashCode()
        result = 31 * result + pickup_location.hashCode()
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
