package com.colivery.engine.service

import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.QueryDocumentSnapshot
import org.springframework.stereotype.Service

@Service
class FireStoreService(private val firestore: Firestore) {

    fun getAllOrderDocumentsWithStatusToBeDelivered(): MutableList<QueryDocumentSnapshot> {

        return firestore.collection("order")
                .whereEqualTo("status", "to_be_delivered")
                .get().get().documents
    }
}

