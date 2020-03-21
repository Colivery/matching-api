package com.colivery.engine

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
        return db.listCollections().count()
    }
}

