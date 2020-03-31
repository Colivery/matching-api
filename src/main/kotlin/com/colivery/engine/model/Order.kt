package com.colivery.engine.model

data class OrderItem(val id: String, val description: String, val status: String)

data class Order(val id: String,
                 val userId: String,
                 val shopName: String?,
                 val pickupAddress: String?,
                 val hint: String?,
                 var shopType: PoIType,
                 val pickupLocation: Coordinate?,
                 val dropOffLocation: Coordinate,
                 val status: String,
                 val driverUserId: String?,
                 val items: List<OrderItem>?
) {
    fun fixType() {
        if (this.shopType == PoIType.Supermarket || this.shopType == PoIType.grocery) {
            this.shopType = PoIType.supermarket
        }
        if (this.shopType == PoIType.Pharmacy) {
            this.shopType = PoIType.pharmacy
        }
    }

    fun buildPickup() = Activity(id,
            pickupLocation!!,
            ActivityType.pickup,
            shopName,
            pickupAddress,
            true
    )

    fun buildDropOff() = Activity(id,
            dropOffLocation,
            ActivityType.drop_off,
            null,
            null,
            null
    )

    fun buildPickup(poi: PoI) = Activity(id,
            poi.coordinate,
            ActivityType.pickup,
            poi.name,
            poi.address,
            false
    )
}