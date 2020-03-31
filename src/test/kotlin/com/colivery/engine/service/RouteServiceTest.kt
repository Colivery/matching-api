package com.colivery.engine.service

import com.colivery.engine.TestConfig
import com.colivery.engine.model.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@Import(TestConfig::class)
internal class RouteServiceTest {

    @Autowired
    private lateinit var routeService: RouteService

    @Test
    fun testBuildRoute1x() {
        val startLocation = Coordinate(49.0, 18.0)
        val order1 = Order("1",
                "1",
                "Shop",
                "Address",
                "hint",
                PoIType.supermarket,
                Coordinate(49.1, 18.1),
                Coordinate(49.2, 18.2),
                "to_be_delivered",
                null,
                emptyList()
        )

        val result = routeService.buildRoute(startLocation,
                listOf(order1),
                emptySet())

        Assertions.assertEquals(3, result.size)

        Assertions.assertEquals(ActivityType.start, result[0].type)
        Assertions.assertEquals(startLocation, result[0].coordinate)

        assertPickup(order1, result[1])
        assertDropoff(order1, result[2])
    }

    @Test
    fun testBuildRoute2x() {
        val startLocation = Coordinate(49.0, 18.0)
        val order1 = Order("1",
                "1",
                "Shop",
                "Address",
                "hint",
                PoIType.supermarket,
                Coordinate(49.1, 18.1),
                Coordinate(49.4, 18.4),
                "to_be_delivered",
                null,
                emptyList())

        val order2 = Order("2",
                "1",
                "Shop",
                "Address",
                "hint",
                PoIType.supermarket,
                Coordinate(49.2, 18.2),
                Coordinate(49.3, 18.3),
                "to_be_delivered",
                null,
                emptyList())

        val result = routeService.buildRoute(startLocation,
                listOf(order1, order2),
                emptySet())

        Assertions.assertEquals(5, result.size)

        Assertions.assertEquals(ActivityType.start, result[0].type)
        Assertions.assertEquals(startLocation, result[0].coordinate)

        assertPickup(order1, result[1])
        assertPickup(order2, result[2])
        assertDropoff(order2, result[3])
        assertDropoff(order1, result[4])
    }

    private fun assertPickup(order: Order, activity: Activity) {
        Assertions.assertEquals(ActivityType.pickup, activity.type)
        Assertions.assertEquals(order.id, activity.orderId)
        Assertions.assertEquals(order.pickupLocation, activity.coordinate)
        Assertions.assertEquals(true, activity.orderRestricted)
    }

    private fun assertDropoff(order: Order, activity: Activity) {
        Assertions.assertEquals(ActivityType.drop_off, activity.type)
        Assertions.assertEquals(order.id, activity.orderId)
        Assertions.assertEquals(order.dropOffLocation, activity.coordinate)
    }
}