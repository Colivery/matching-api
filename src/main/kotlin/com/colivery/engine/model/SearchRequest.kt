package com.colivery.engine.model

data class SearchRequest(val coordinate: Coordinate, val range: Float = 5.0F)