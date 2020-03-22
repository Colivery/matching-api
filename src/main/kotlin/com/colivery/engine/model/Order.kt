package com.colivery.engine.model

data class Order(val id: String,
                 val userId: String,
                 val shopName: String?,
                 val pickupAddress: String?,
                 val shopType: PoIType,
                 val pickupLocation: Coordinate?,
                 val dropOffLocation: Coordinate
)