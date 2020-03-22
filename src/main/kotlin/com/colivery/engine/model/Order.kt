package com.colivery.engine.model

data class Order(val id: String,
                 val userId: String,
                 val shopName: String?,
                 val pickupAddress: String?,
                 var shopType: PoIType,
                 val pickupLocation: Coordinate?,
                 val dropOffLocation: Coordinate
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Order

        if (id != other.id) return false
        if (userId != other.userId) return false
        if (shopName != other.shopName) return false
        if (pickupAddress != other.pickupAddress) return false
        if (shopType != other.shopType) return false
        if (pickupLocation != other.pickupLocation) return false
        if (dropOffLocation != other.dropOffLocation) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + userId.hashCode()
        result = 31 * result + (shopName?.hashCode() ?: 0)
        result = 31 * result + (pickupAddress?.hashCode() ?: 0)
        result = 31 * result + shopType.hashCode()
        result = 31 * result + (pickupLocation?.hashCode() ?: 0)
        result = 31 * result + dropOffLocation.hashCode()
        return result
    }


    fun fixType() {
        if (this.shopType == PoIType.Supermarket || this.shopType == PoIType.grocery) {
            this.shopType = PoIType.supermarket
        }
        if (this.shopType == PoIType.Pharmacy) {
            this.shopType = PoIType.pharmacy
        }
    }
}