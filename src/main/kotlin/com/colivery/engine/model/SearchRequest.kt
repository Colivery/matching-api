package com.colivery.engine.model

import javax.validation.constraints.Max
import javax.validation.constraints.Min

data class SearchRequest(val coordinate: Coordinate) {
    @Min(0)
    @Max(10)
    val range: Float = 5.0F
}