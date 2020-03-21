package com.colivery.engine

import com.colivery.engine.model.Order
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.Firestore
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

    fun getOrderCount(): Int {

        // Create a reference to the cities collection
        val ordersRef = db.collection("order")

        // Create a query against the collection.
        val query = ordersRef.whereEqualTo("status", "to_be_delivered")

        val querySnapshot = query.get()

        for (document in querySnapshot.get().documents) {

        }


        return querySnapshot.get().documents.count()
    }
}

