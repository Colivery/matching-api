package com.colivery.engine.model

import org.springframework.validation.annotation.Validated
import javax.validation.constraints.Max
import javax.validation.constraints.Min

@Validated
data class Coordinate(
        @Min(-90)
        @Max(90)
        val latitude: Double,

        @Min(-180)
        @Max(80)
        val longitude: Double
)


