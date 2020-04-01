package com.colivery.engine.service.poi

import com.colivery.engine.model.PoI
import com.colivery.engine.model.PoIType
import com.colivery.geo.Coordinate

interface PoiSearchService {
    fun findPoIs(position: Coordinate, radiusKm: Float, poiTypes: Set<PoIType>): List<PoI>
}