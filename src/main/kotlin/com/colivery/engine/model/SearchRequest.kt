package com.colivery.engine.model

import org.springframework.validation.annotation.Validated
import javax.validation.Valid
import javax.validation.constraints.Max
import javax.validation.constraints.Min

@Validated
data class SearchRequest(
        @Valid
        val coordinate: Coordinate,

        @Min(0)
        @Max(10)
        val range: Float = 5.0F
)