package com.colivery.engine.model

import com.colivery.geo.Coordinate
import org.springframework.validation.annotation.Validated
import javax.validation.Valid
import javax.validation.constraints.Max
import javax.validation.constraints.Min

@Validated
data class QueryRequest(
        @Valid
        val coordinate: Coordinate,

        @Min(1)
        @Max(50)
        val range: Float = 5.0F
)