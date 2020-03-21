package com.colivery.engine.model

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

data class GeoPosition(val lat: Double, val lon: Double)

data class Order(val id: String,
                 val user_id: String,
                 val driver_user_id: String,
                 val shop_name: String,
                 val shop_type: String,
                 val pickup_address: String,
                 val pickup_location: GeoPosition,
                 val products: Array<OrderItem>,
                 val status: OrderStatus,
                 val hint: String,
                 val created: String,
                 val updated: String) {

}


enum class OrderItemStatus {
    todo, done, na
}

data class OrderItem(val id: String,
                     val description: String,
                     val status: OrderItemStatus)
