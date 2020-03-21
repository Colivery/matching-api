package com.colivery.engine.model

data class SearchResult(val order_ids: List<String>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SearchResult

        if (order_ids != other.order_ids) return false

        return true
    }

    override fun hashCode(): Int {
        return order_ids.hashCode()
    }

}