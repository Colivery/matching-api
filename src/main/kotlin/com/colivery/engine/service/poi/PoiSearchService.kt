package com.colivery.engine.service.poi

import com.colivery.engine.model.Coordinate
import com.colivery.engine.service.PoI

interface PoiSearchService {
    fun findPoIs(position: Coordinate, radiusKm: Float): Array<PoI>
}