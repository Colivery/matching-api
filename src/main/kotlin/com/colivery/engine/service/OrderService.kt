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
    lateinit var geoHashService: GeoHashService

    @Autowired
    lateinit var distanceService: DistanceService

    fun fetchOrdersByIds(orderIds: Set<String>): List<Order> {
        val orders = fireStoreService.getOrdersByIds(orderIds)
                .mapNotNull { documentSnapshot -> filterValidOrderDocuments(documentSnapshot) }
        orders.forEach { it.fixType() }
        return orders
    }

    fun fetchAllValidOrders(startLocation: Coordinate, radius: Float): List<Order> {
        val geoHashes = buildGeoHashes(startLocation, radius)

        val orders = fireStoreService.getOrderDocumentsByStatusAndGeoHash(FireStoreService.Status.to_be_delivered, geoHashes)
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

    fun buildGeoHashes(startLocation: Coordinate, radius: Float): MutableSet<String> {
        val initialGeoHash = geoHashService.encode(startLocation.latitude, startLocation.longitude)
        val bounds = geoHashService.bounds(initialGeoHash)
        val neighbours = geoHashService.neighbours(initialGeoHash)

        val geoHashes = mutableSetOf(initialGeoHash)
        testE(startLocation, radius, bounds, geoHashes, neighbours.e)
        testSE(startLocation, radius, bounds, geoHashes, neighbours.se)
        testS(startLocation, radius, bounds, geoHashes, neighbours.s)
        testSW(startLocation, radius, bounds, geoHashes, neighbours.sw)
        testW(startLocation, radius, bounds, geoHashes, neighbours.w)
        testNW(startLocation, radius, bounds, geoHashes, neighbours.nw)
        testN(startLocation, radius, bounds, geoHashes, neighbours.n)
        testNE(startLocation, radius, bounds, geoHashes, neighbours.ne)

        return geoHashes
    }

    private fun testE(startLocation: Coordinate, radius: Float, bounds: Bounds, geoHashes: MutableSet<String>, geoHash: String) {
        if (distanceService.haversine(startLocation, Coordinate(startLocation.latitude, bounds.ne.longitude)) < radius) {
            geoHashes.add(geoHash)
            testE(startLocation, radius, geoHashService.bounds(geoHash), geoHashes, geoHashService.neighbours(geoHash).e)
        }
    }

    private fun testSE(startLocation: Coordinate, radius: Float, bounds: Bounds, geoHashes: MutableSet<String>, geoHash: String) {
        if (distanceService.haversine(startLocation, Coordinate(bounds.sw.latitude, bounds.ne.longitude)) < radius) {
            geoHashes.add(geoHash)
            testSE(startLocation, radius, geoHashService.bounds(geoHash), geoHashes, geoHashService.neighbours(geoHash).se)
            testS(startLocation, radius, geoHashService.bounds(geoHash), geoHashes, geoHashService.neighbours(geoHash).s)
            testE(startLocation, radius, geoHashService.bounds(geoHash), geoHashes, geoHashService.neighbours(geoHash).e)
        }
    }

    private fun testS(startLocation: Coordinate, radius: Float, bounds: Bounds, geoHashes: MutableSet<String>, geoHash: String) {
        if (distanceService.haversine(startLocation, Coordinate(bounds.sw.latitude, startLocation.longitude)) < radius) {
            geoHashes.add(geoHash)
            testS(startLocation, radius, geoHashService.bounds(geoHash), geoHashes, geoHashService.neighbours(geoHash).s)
        }
    }

    private fun testSW(startLocation: Coordinate, radius: Float, bounds: Bounds, geoHashes: MutableSet<String>, geoHash: String) {
        if (distanceService.haversine(startLocation, Coordinate(bounds.sw.latitude, bounds.sw.longitude)) < radius) {
            geoHashes.add(geoHash)
            testSW(startLocation, radius, geoHashService.bounds(geoHash), geoHashes, geoHashService.neighbours(geoHash).sw)
            testS(startLocation, radius, geoHashService.bounds(geoHash), geoHashes, geoHashService.neighbours(geoHash).s)
            testW(startLocation, radius, geoHashService.bounds(geoHash), geoHashes, geoHashService.neighbours(geoHash).w)
        }
    }

    private fun testW(startLocation: Coordinate, radius: Float, bounds: Bounds, geoHashes: MutableSet<String>, geoHash: String) {
        if (distanceService.haversine(startLocation, Coordinate(startLocation.latitude, bounds.sw.longitude)) < radius) {
            geoHashes.add(geoHash)
            testW(startLocation, radius, geoHashService.bounds(geoHash), geoHashes, geoHashService.neighbours(geoHash).w)
        }
    }

    private fun testNW(startLocation: Coordinate, radius: Float, bounds: Bounds, geoHashes: MutableSet<String>, geoHash: String) {
        if (distanceService.haversine(startLocation, Coordinate(bounds.ne.latitude, bounds.sw.longitude)) < radius) {
            geoHashes.add(geoHash)
            testNW(startLocation, radius, geoHashService.bounds(geoHash), geoHashes, geoHashService.neighbours(geoHash).nw)
            testN(startLocation, radius, geoHashService.bounds(geoHash), geoHashes, geoHashService.neighbours(geoHash).n)
            testW(startLocation, radius, geoHashService.bounds(geoHash), geoHashes, geoHashService.neighbours(geoHash).w)
        }
    }

    private fun testN(startLocation: Coordinate, radius: Float, bounds: Bounds, geoHashes: MutableSet<String>, geoHash: String) {
        if (distanceService.haversine(startLocation, Coordinate(bounds.ne.latitude, startLocation.longitude)) < radius) {
            geoHashes.add(geoHash)
            testN(startLocation, radius, geoHashService.bounds(geoHash), geoHashes, geoHashService.neighbours(geoHash).n)
        }
    }

    private fun testNE(startLocation: Coordinate, radius: Float, bounds: Bounds, geoHashes: MutableSet<String>, geoHash: String) {
        if (distanceService.haversine(startLocation, Coordinate(bounds.ne.latitude, bounds.ne.longitude)) < radius) {
            geoHashes.add(geoHash)
            testNE(startLocation, radius, geoHashService.bounds(geoHash), geoHashes, geoHashService.neighbours(geoHash).ne)
            testN(startLocation, radius, geoHashService.bounds(geoHash), geoHashes, geoHashService.neighbours(geoHash).n)
            testE(startLocation, radius, geoHashService.bounds(geoHash), geoHashes, geoHashService.neighbours(geoHash).e)
        }
    }
}