package com.colivery.engine.service

import com.colivery.engine.model.Coordinate
import com.colivery.engine.model.Order
import com.colivery.engine.toOrder
import com.google.cloud.firestore.QueryDocumentSnapshot
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class OrderService {

    private val logger: Logger = LoggerFactory.getLogger(OrderService::class.java)

    @Autowired
    lateinit var fireStoreService: FireStoreService

    @Autowired
    lateinit var distanceService: DistanceService

    fun fetchAllValidOrders(startLocation: Coordinate, radius: Float): List<Order> {
        val orders = fireStoreService.getAllOrderDocumentsWithStatusToBeDelivered()
                .mapNotNull { documentSnapshot -> filterValidOrderDocuments(documentSnapshot) }
                .filter { order ->
                    if (order.pickupLocation == null)
                        true
                    else
                        distanceService.haversine(startLocation, order.pickupLocation) <= radius
                }
                .filter { order -> distanceService.haversine(startLocation, order.dropOffLocation) <= radius }
        orders.forEach { it.fixType() }
        return orders
    }

    private fun filterValidOrderDocuments(documentSnapshot: QueryDocumentSnapshot): Order? {
        return try {
            documentSnapshot.toOrder()
        } catch (e: Throwable) {
            logger.info("Problem parsing Order (invalid document?) / OrderID " + documentSnapshot.id)
            e.printStackTrace()
            null
        }
    }
}