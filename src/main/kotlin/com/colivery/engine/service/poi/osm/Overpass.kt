package com.colivery.engine.service.poi.osm

import com.colivery.engine.model.Coordinate
import com.colivery.engine.service.PoI
import com.colivery.engine.service.PoIType
import com.colivery.engine.service.poi.PoiSearchService
import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@JsonIgnoreProperties(ignoreUnknown = true)
data class Response(val elements: Array<Element>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Response

        if (!elements.contentEquals(other.elements)) return false

        return true
    }

    override fun hashCode(): Int {
        return elements.contentHashCode()
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class Element(val tags: Tag, val lat: Double, val lon: Double)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Tag(val shop: String?, val name: String?, val amenity: String?) {
    @JsonAlias("addr:street")
    val street: String = ""
    @JsonAlias("addr:housenumber")
    val number: String = ""
    @JsonAlias("addr:postcode")
    val postcode: String = ""
    @JsonAlias("addr:city")
    val city: String = ""
    @JsonAlias("addr:country")
    val country: String = ""
}

@Service
class Overpass : PoiSearchService {
    override fun findPoIs(position: Coordinate, radiusKm: Float): Array<PoI> {
        val quote: Response? = RestTemplate().postForObject("https://overpass-api.de/api/interpreter",
                getRequestBody(position, radiusKm),
                Response::class.java)

        if (quote != null) {
            return quote.elements
                    .map { element -> buildPoi(element) }
                    .toTypedArray()
        }
        return emptyArray()
    }

    private fun buildPoi(element: Element): PoI {
        return PoI(if (element.tags.shop != null) PoIType.Supermarket else PoIType.Pharmacy,
                Coordinate(element.lat, element.lon),
                element.tags.street + " " + element.tags.number + " " + element.tags.postcode + " " + element.tags.city,
                element.tags.name ?: "")
    }

    private fun getRequestBody(position: Coordinate, radiusKm: Float): String {
        var degreeDelta = radiusKm / 111.0F
        var latN = position.latitude - degreeDelta
        var latS = position.latitude + degreeDelta

        var lonL = position.longitude - degreeDelta
        var lonR = position.longitude + degreeDelta

        return "[out:json]\n" +
                "[bbox:" + latN + "," + lonL + "," + latS + "," + lonR + "];\n" +
                "( nwr[amenity=pharmacy];\n" +
                "  nwr[shop=supermarket];\n" +
                //"  nwr[shop=convenience];\n" +
                " );\n" +
                "out center;"
    }
}