package com.colivery.engine

import com.colivery.engine.model.Coordinate
import com.colivery.engine.model.Order
import com.colivery.engine.model.PoIType
import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.GeoPoint
import com.google.cloud.firestore.QueryDocumentSnapshot
import org.springframework.stereotype.Service

@Service
class FireStoreService(private val firestore: Firestore) {

    fun getAllOrdersWithStateToBeDelivered(): List<Order> {

        val querySnapshot = firestore.collection("order")
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

