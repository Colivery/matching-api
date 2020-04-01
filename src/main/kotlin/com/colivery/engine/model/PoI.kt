package com.colivery.engine.model

import com.colivery.geo.Coordinate

enum class PoIType {
    Supermarket, Pharmacy, grocery, butcher, bakery, cafe, pharmacy, supermarket, beverages
}

data class PoI(val type: PoIType, val coordinate: Coordinate, val address: String, val name: String)