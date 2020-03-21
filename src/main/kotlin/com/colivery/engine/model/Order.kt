package com.colivery.engine.model

import com.colivery.engine.service.PoIType
import com.google.cloud.firestore.GeoPoint

data class Order(val id: String,
                 val userId: String,
                 val shopName: String?,
                 val shopType: PoIType,
                 val pickupAddress: String?,
                 val pickupLocation: GeoPoint?
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Order

        if (id != other.id) return false
        if (userId != other.userId) return false
        if (shopName != other.shopName) return false
        if (shopType != other.shopType) return false
        if (pickupAddress != other.pickupAddress) return false
        if (pickupLocation != other.pickupLocation) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + userId.hashCode()
        result = 31 * result + shopName.hashCode()
        result = 31 * result + shopType.hashCode()
        result = 31 * result + pickupAddress.hashCode()
        result = 31 * result + pickupLocation.hashCode()
        return result
    }

}