package com.colivery.engine.model

import com.google.cloud.firestore.GeoPoint

data class SearchRequest(val coordinate: Coordinate,
                         val range: Float = 10.0F)