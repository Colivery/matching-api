package com.colivery.engine.model

import com.google.cloud.firestore.GeoPoint

data class SearchRequest(val position: Coordinate,
                         val radiusKm: Float = 10.0F)