package com.colivery.engine.service

import com.google.cloud.firestore.FieldPath
import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.QueryDocumentSnapshot
import org.springframework.stereotype.Service
import kotlin.math.max


const val ORDER_ITEM_COLLECTION_NAME = "items"
const val ORDER_COLLECTION_NAME = "order"

@Service
class FireStoreService(private val firestore: Firestore) {

    enum class Status { to_be_delivered }

    val orderCollection = firestore.collection("order")

    fun getOrderDocumentsByStatusAndGeoHash(status: Status, geoHashes: Set<String>): MutableList<QueryDocumentSnapshot> {
        val result: MutableList<QueryDocumentSnapshot> = mutableListOf()

        for (chunk in geoHashes.chunked(10))
            result.addAll(orderCollection
                    .whereEqualTo("status", status.name)
                    .whereIn("dropoff_location_geohash", chunk.toMutableList())
                    .get().get().documents)

        return result
    }

    fun getOrderDocumentsByStatusAndGeoHashRange(status: Status, minGeoHash: String, maxGeoHash: String):
            MutableList<QueryDocumentSnapshot> {

        return orderCollection
                .whereEqualTo("status", status.name)
                .whereGreaterThanOrEqualTo("dropoff_location_geohash", minGeoHash)
                .whereLessThanOrEqualTo("dropoff_location_geohash", maxGeoHash)
                .get().get().documents
    }

    fun getOrdersByIds(orderIds: Set<String>): MutableList<QueryDocumentSnapshot> {
        return firestore.collection(ORDER_COLLECTION_NAME)
                .whereIn(FieldPath.documentId(), orderIds.toList())
                .get().get().documents
    }

    fun getOrderItems(orderId: String): MutableList<QueryDocumentSnapshot> {
        return firestore.collection(ORDER_COLLECTION_NAME).document(orderId).collection(ORDER_ITEM_COLLECTION_NAME)
                .get().get().documents
    }
}
