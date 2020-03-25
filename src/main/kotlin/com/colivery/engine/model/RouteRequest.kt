package com.colivery.engine.model

import javax.validation.Valid
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class RouteRequest(
        @Valid
        val coordinate: Coordinate,

        @Min(0)
        @Max(50)
        val range: Float = 5.0F,

        @NotNull
        @NotEmpty
        val orderIds: Set<String>
)