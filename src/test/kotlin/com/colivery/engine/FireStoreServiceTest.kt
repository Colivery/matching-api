package com.colivery.engine

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class FireStoreServiceTest {

    @Test
    fun getOrders() {
        assertEquals(1,FireStoreService().getOrders().count())
    }
}