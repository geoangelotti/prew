package com.example.prew.controller

import com.example.prew.dto.Edge
import com.example.prew.dto.Node
import com.example.prew.errors.ErrorResponse
import com.example.prew.service.Service
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
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
    @Operation(
        summary = "Create a new edge",
        description = "Creates a new edge between two nodes"
    )
    @ApiResponses(value = [
        ApiResponse(
            responseCode = "201",
            description = "Edge created successfully",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = Edge::class)
            )]
        ),
        ApiResponse(
            responseCode = "400",
            description = "Invalid edge data - validation error",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = ErrorResponse::class)
            )]
        ),
        ApiResponse(
            responseCode = "409",
            description = "Edge already exists",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = ErrorResponse::class)
            )]
        ),
        ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = ErrorResponse::class)
            )]
        )
    ])
    fun createEdge(@RequestBody edge: Edge<Int>): ResponseEntity<Any> {
        val uri = URI.create("/${edge.from}/${edge.to}")
        val result = service.createEdge(edge)
        return result.fold(
            onSuccess = { ResponseEntity.created(uri).body(edge) },
            onFailure = { error -> throw error }
        )
    }

    @DeleteMapping("/{from}/{to}")
    @Operation(
        summary = "Delete an edge",
        description = "Deletes an edge between two nodes"
    )
    @ApiResponses(value = [
        ApiResponse(
            responseCode = "204",
            description = "Edge deleted successfully"
        ),
        ApiResponse(
            responseCode = "404",
            description = "Edge not found",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = ErrorResponse::class)
            )]
        ),
        ApiResponse(
            responseCode = "400",
            description = "Invalid parameters",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = ErrorResponse::class)
            )]
        ),
        ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = ErrorResponse::class)
            )]
        )
    ])
    fun deleteTree(@PathVariable from: Int, @PathVariable to: Int): ResponseEntity<Any> {
        val edge = Edge(from, to)
        val result = service.deleteEdge(edge)
        return result.fold(
            onSuccess = { ResponseEntity.noContent().build() },
            onFailure = { error -> throw error }
        )
    }

    @GetMapping("/{id}/tree")
    @Operation(
        summary = "Get tree structure",
        description = "get the tree starting from the node"
    )
    @ApiResponses(value = [
        ApiResponse(
            responseCode = "200",
            description = "Tree retrieved successfully",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = Node::class)
            )]
        ),
        ApiResponse(
            responseCode = "404",
            description = "Node not found",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = ErrorResponse::class)
            )]
        ),
        ApiResponse(
            responseCode = "400",
            description = "Invalid node ID",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = ErrorResponse::class)
            )]
        ),
        ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = [Content(
                mediaType = "application/json",
                schema = Schema(implementation = ErrorResponse::class)
            )]
        )
    ])
    fun getTree(@PathVariable id: Int): ResponseEntity<Any> {
        val result = service.getTree(id)
        return result.fold(
            onSuccess = { ResponseEntity.ok().body(result.getOrNull()) },
            onFailure = { error -> throw error }
        )
    }
}