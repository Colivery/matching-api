package com.colivery.engine

import com.colivery.engine.model.Order
import com.colivery.engine.service.PoIType
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.GeoPoint
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.cloud.FirestoreClient


class FireStoreService {

    val db: Firestore

    init {
        val credentials = GoogleCredentials.getApplicationDefault()

        val options = FirebaseOptions.Builder()
                .setCredentials(credentials)
                .setProjectId("colivery-app")
                .build()

        FirebaseApp.initializeApp(options)
        db = FirestoreClient.getFirestore();
    }

    fun getOrders(): List<Order> {

        val querySnapshot = db.collection("order")
                .whereEqualTo("status", "to_be_delivered")
                .get()

        return querySnapshot.get().documents.map { map ->
            Order(
                    "1",
                    map.get("user_id") as String,
                    map.get("shop_name") as String,
                    PoIType.valueOf(map.get("shop_type") as String),
                    map.get("pickup_address") as String,
                    map.get("pickup_location") as GeoPoint

            )
        }.toList()
    }
}

