package com.colivery.engine.service

import com.colivery.engine.model.Order
import com.colivery.engine.toOrder
import com.colivery.engine.toOrderItem
import com.colivery.geo.Coordinate
import com.colivery.geo.Distance
import com.colivery.geo.GeoHash
import com.google.cloud.firestore.DocumentSnapshot
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class OrderService {

    private val logger: Logger = LoggerFactory.getLogger(OrderService::class.java)

    @Autowired
    lateinit var fireStoreService: FireStoreService

    fun fetchOrdersByIds(orderIds: Set<String>): List<Order> {
        val orders = fireStoreService.getOrdersByIds(orderIds)
                .mapNotNull { documentSnapshot -> filterValidOrderDocuments(documentSnapshot) }
        orders.forEach { it.fixType() }
        return orders
    }

    fun fetchAllValidOrders(startLocation: Coordinate, radius: Float): List<Order> {
        val (minGeoHash, maxGeoHash) = GeoHash.buildMinMaxGeoHashesOfCircle(startLocation, radius)

        val orders = fireStoreService.getOrderDocumentsByStatusAndGeoHashRange(
                FireStoreService.Status.to_be_delivered, minGeoHash, maxGeoHash)
                .mapNotNull { documentSnapshot -> filterValidOrderDocuments(documentSnapshot) }
                .filter { order ->
                    if (order.pickupLocation == null)
                        true
                    else
                        Distance.haversine(startLocation, order.pickupLocation) <= radius
                }
                .filter { order -> Distance.haversine(startLocation, order.dropoffLocation) <= radius }
        orders.forEach { it.fixType() }
        return orders
    }

    private fun filterValidOrderDocuments(documentSnapshot: DocumentSnapshot): Order? {
        return try {
            documentSnapshot.toOrder(
                    fireStoreService.getOrderItems(documentSnapshot.id)
                            .map { it.toOrderItem() }
            )
        } catch (e: Throwable) {
            logger.info("Problem parsing Order (invalid document?) / OrderID " + documentSnapshot.id)
            e.printStackTrace()
            null
        }
    }

}