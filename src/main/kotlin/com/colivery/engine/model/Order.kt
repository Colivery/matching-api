package com.colivery.engine.model

data class Order(val id: String,
                 val userId: String,
                 val shopName: String?,
                 val pickupAddress: String?,
                 var shopType: PoIType,
                 val pickupLocation: Coordinate?,
                 val dropOffLocation: Coordinate
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