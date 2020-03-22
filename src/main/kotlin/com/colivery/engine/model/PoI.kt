package com.colivery.engine.model

enum class PoIType {
    Supermarket, Pharmacy, grocery
}

data class PoI(val type: PoIType, val coordinate: Coordinate, val address: String, val name: String)