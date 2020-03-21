package com.colivery.engine

import com.colivery.engine.model.SearchRequest
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.naming.directory.SearchResult

@RestController
class EngineController {
    @PostMapping("/search")
    fun search(@RequestBody request: SearchRequest): SearchResult? {
        return null
    }
}