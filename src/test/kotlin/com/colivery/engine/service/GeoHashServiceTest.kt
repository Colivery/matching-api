package com.colivery.engine.service

import com.colivery.engine.TestConfig
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@Import(TestConfig::class)
internal class GeoHashServiceTest {

    private val geoHashService = GeoHashService()

    @Test
    fun encode() {
        val geoHash = geoHashService.encode(48.669467, -4.329468)
        assertEquals("gbsuv", geoHash)
    }

    @Test
    fun neighbours() {
        val neighbors = geoHashService.neighbours("gbsuv")

        assertEquals("gbsvh", neighbors.nw)
        assertEquals("gbsvj", neighbors.n)
        assertEquals("gbsvn", neighbors.ne)
        assertEquals("gbsuu", neighbors.w)
        assertEquals("gbsuy", neighbors.e)
        assertEquals("gbsus", neighbors.sw)
        assertEquals("gbsut", neighbors.s)
        assertEquals("gbsuw", neighbors.se)
    }
}