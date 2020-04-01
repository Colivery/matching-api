package com.colivery.engine.service.poi.osm

import com.colivery.engine.model.PoI
import com.colivery.engine.model.PoIType
import com.colivery.engine.service.poi.PoiSearchService
import com.colivery.geo.Coordinate
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
    override fun findPoIs(position: Coordinate, radiusKm: Float, poiTypes: Set<PoIType>): List<PoI> {
        val quote: Response? = RestTemplate().postForObject("https://overpass-api.de/api/interpreter",
                getRequestBody(position, radiusKm, poiTypes),
                Response::class.java)

        if (quote != null) {
            return quote.elements
                    .map { element -> buildPoi(element) }
        }
        return emptyList()
    }

    private fun buildPoi(element: Element): PoI {
        return PoI(translatePoIType(element),
                Coordinate(element.lat, element.lon),
                element.tags.street + " " + element.tags.number + " " + element.tags.postcode + " " + element.tags.city,
                element.tags.name ?: "")
    }

    private fun translatePoIType(element: Element): PoIType {
        if (!element.tags.amenity.isNullOrEmpty()) {
            if (element.tags.amenity == "cafe") {
                return PoIType.cafe
            }
            if (element.tags.amenity == "pharmacy") {
                return PoIType.pharmacy
            }
        }
        if (element.tags.shop == "bakery") {
            return PoIType.bakery
        }
        if (element.tags.shop == "butcher") {
            return PoIType.butcher
        }
        if (element.tags.shop == "cafe") {
            return PoIType.cafe
        }
        if (element.tags.shop == "beverages") {
            return PoIType.beverages
        }
        return PoIType.supermarket
    }

    private fun getRequestBody(position: Coordinate, radiusKm: Float, poiTypes: Set<PoIType>): String {
        var degreeDelta = radiusKm / 111.0F
        var latN = position.latitude - degreeDelta
        var latS = position.latitude + degreeDelta

        var lonL = position.longitude - degreeDelta
        var lonR = position.longitude + degreeDelta

        return "[out:json][timeout:25]\n" +
                "[bbox:" + latN + "," + lonL + "," + latS + "," + lonR + "];\n" +
                "(" +
                buildOSMPoITypeRequestString(poiTypes) +
                " );\n" +
                "out center;"
    }

    private fun buildOSMPoITypeRequestString(poiTypes: Set<PoIType>): String {
        val result = StringBuilder()
        poiTypes.forEach {
            if (it == PoIType.Pharmacy || it == PoIType.pharmacy) {
                result.append("  nwr[amenity=pharmacy];\n")
            } else if (it == PoIType.Supermarket || it == PoIType.grocery || it == PoIType.supermarket) {
                result.append("  nwr[shop=supermarket];\n")
            } else if (it == PoIType.bakery) {
                result.append("  nwr[shop=bakery];\n")
            } else if (it == PoIType.butcher) {
                result.append("  nwr[shop=butcher];\n")
            } else if (it == PoIType.cafe) {
                result.append("  nwr[amenity=cafe];\n")
            } else if (it == PoIType.beverages) {
                result.append("  nwr[shop=beverages];\n")
            }
        }
        return result.toString()
    }
}