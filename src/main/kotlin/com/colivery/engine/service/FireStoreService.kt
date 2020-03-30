package com.colivery.engine.service

import com.google.cloud.firestore.FieldPath
import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.QueryDocumentSnapshot
import org.springframework.stereotype.Service

@Service
class FireStoreService(private val firestore: Firestore) {
    enum class Status { to_be_delivered }

    fun getOrderDocuments(status: Status, geoHashes: Set<String>): MutableList<QueryDocumentSnapshot> {
        val result: MutableList<QueryDocumentSnapshot> = mutableListOf()

        for (chunk in geoHashes.chunked(10))
            result.addAll(firestore.collection("order")
                    .whereEqualTo("status", status.name)
                    .whereIn("dropoff_location_geohash", chunk.toMutableList())
                    .get().get().documents)

        return result
    }

    fun getOrdersByIds(orderIds: Set<String>): MutableList<QueryDocumentSnapshot> {
        return firestore.collection("order")
                .whereIn(FieldPath.documentId(), orderIds.toList())
                .get().get().documents
    }
}
