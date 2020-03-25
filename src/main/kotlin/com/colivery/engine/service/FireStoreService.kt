package com.colivery.engine.service

import com.google.cloud.firestore.FieldPath
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

    fun getOrdersByIds(orderIds: Set<String>): MutableList<QueryDocumentSnapshot> {
        return firestore.collection("order")
                .whereIn(FieldPath.documentId(), orderIds.toList())
                .get().get().documents
    }
}

