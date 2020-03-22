package com.colivery.engine.model

enum class PoIType {
    Supermarket, Pharmacy, grocery, butcher, bakery, cafe, pharmacy, supermarket, beverages
}

data class PoI(val type: PoIType, val coordinate: Coordinate, val address: String, val name: String)