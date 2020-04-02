package com.colivery.engine.model

import com.colivery.geo.Coordinate
import org.springframework.validation.annotation.Validated
import javax.validation.Valid
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Validated
data class RouteRequest(
        @Valid
        val coordinate: Coordinate,

        @Min(1)
        @Max(50)
        val range: Double?,

        @NotNull
        @Size(min = 1, max = 10)
        val orderIds: Set<String>
)