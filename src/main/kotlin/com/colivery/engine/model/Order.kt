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
}