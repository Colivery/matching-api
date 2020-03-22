package com.colivery.engine.service

import com.colivery.engine.model.Order
import com.colivery.engine.toOrder
import com.google.cloud.firestore.Firestore
import org.springframework.stereotype.Service

@Service
class FireStoreService(private val firestore: Firestore) {

    fun getAllOrdersWithStateToBeDelivered(): List<Order> {

        val querySnapshot = firestore.collection("order")
                .whereEqualTo("status", "to_be_delivered")
                .get()

        return querySnapshot.get().documents
                .map { documentSnapshot -> documentSnapshot.toOrder() }
                .toList()
    }
}

