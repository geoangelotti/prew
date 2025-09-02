package com.example.prew.controller

import com.example.prew.dto.Edge
import com.example.prew.service.Service
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/edges")
class Controller(private val service: Service) {
    @PostMapping
    fun createEdge(@RequestBody edge: Edge<Int>): ResponseEntity<Any> {
        val uri = URI.create("/${edge.from}/${edge.to}")
        val result = service.createEdge(edge)
        return result.fold(
            onSuccess = { ResponseEntity.created(uri).body(edge) },
            onFailure = { error -> throw error }
        )
    }

    @DeleteMapping("/{from}/{to}")
    fun deleteTree(@PathVariable from: Int, @PathVariable to: Int): ResponseEntity<Any> {
        val edge = Edge(from, to)
        val result = service.deleteEdge(edge)
        return result.fold(
            onSuccess = { ResponseEntity.noContent().build() },
            onFailure = { error -> throw error }
        )
    }

    @GetMapping("/{id}/tree")
    fun getTree(@PathVariable id: Int): ResponseEntity<Any> {
        val result = service.getTree(id)
        return result.fold(
            onSuccess = { ResponseEntity.ok().body(result.getOrNull()) },
            onFailure = { error -> throw error }
        )
    }
}