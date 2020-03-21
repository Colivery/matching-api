package com.colivery.engine.service.poi

import com.colivery.engine.service.PoI
import com.google.cloud.firestore.GeoPoint

interface PoiSearchService {
    fun findPoIs(position: GeoPoint, radiusKm: Float): Array<PoI>
}