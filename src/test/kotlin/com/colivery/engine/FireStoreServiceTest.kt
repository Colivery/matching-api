package com.colivery.engine

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class FireStoreServiceTest {

    //@Test
    fun getAllOrdersWithStateToBeDelivered() {
        assertEquals(1, FireStoreService().getAllOrdersWithStateToBeDelivered().count())
    }
}