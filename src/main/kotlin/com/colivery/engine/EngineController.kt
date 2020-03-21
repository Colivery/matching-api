package com.colivery.engine

import com.colivery.engine.model.SearchRequest
import com.colivery.engine.service.PoIService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.naming.directory.SearchResult

@RestController
class EngineController {
    @Autowired
    lateinit var poiService: PoIService

    @PostMapping("/search")
    fun search(@RequestBody request: SearchRequest): SearchResult? {
        return null
    }
}