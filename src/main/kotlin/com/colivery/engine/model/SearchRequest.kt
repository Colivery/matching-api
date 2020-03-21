package com.colivery.engine.model

data class SearchRequest(val position: Coordinate, val radiusKm: Float = 5.0F)