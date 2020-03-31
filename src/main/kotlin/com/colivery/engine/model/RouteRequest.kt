package com.colivery.engine.model

import org.springframework.validation.annotation.Validated
import javax.validation.Valid
import javax.validation.constraints.*

@Validated
data class RouteRequest(
        @Valid
        val coordinate: Coordinate,

        @Min(1)
        @Max(50)
        val range: Float? = 5.0F,

        @NotNull
        @Size(min = 1, max = 10)
        val orderIds: Set<String>
)