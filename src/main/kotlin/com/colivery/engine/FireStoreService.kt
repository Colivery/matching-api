package com.colivery.engine

import com.colivery.engine.model.Coordinate
import com.colivery.engine.model.Order
import com.colivery.engine.model.PoIType
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.GeoPoint
import com.google.cloud.firestore.QueryDocumentSnapshot
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.cloud.FirestoreClient
import org.springframework.stereotype.Service

@Service
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

    fun getAllOrdersWithStateToBeDelivered(): List<Order> {

        val querySnapshot = db.collection("order")
                .whereEqualTo("status", "to_be_delivered")
                .get()

        return querySnapshot.get().documents.map { order -> mapDocumentToOrder(order) }.toList()
    }

    fun mapDocumentToOrder(document: QueryDocumentSnapshot): Order {
        val pickupLocation = document.get("pickup_location") as GeoPoint?
        val dropOffLocation = document.get("dropoff_location") as GeoPoint?


        return Order(
                document.reference.id,
                (document.get("user_id") as String?)!!,
                document.get("shop_name") as String?,
                document.get("pickup_address") as String?,
                PoIType.valueOf((document.get("shop_type") as String?)!!),
                geoPointToCoordinate(pickupLocation),
                Coordinate(dropOffLocation!!.latitude, dropOffLocation.longitude)
        )
    }

    fun geoPointToCoordinate(geoPoint: GeoPoint?): Coordinate? {
        return if (geoPoint != null) {
            Coordinate(geoPoint.latitude, geoPoint.longitude)
        } else {
            null
        }
    }
}

