package com.colivery.engine.model

data class SearchRequest(val position: GeoPosition,
                         val radiusKm: Float = 10.0F)