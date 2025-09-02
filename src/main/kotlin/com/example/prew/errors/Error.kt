package com.example.prew.errors

import com.example.prew.dto.Edge
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

data class ErrorResponse(
    val message: String?,
    val timestamp: String = java.time.Instant.now().toString(),
    val path: String? = null
)

sealed class Error(override val message: String) : Exception(message) {
    abstract fun toHttpResponse(path: String?): ResponseEntity<ErrorResponse>

    class AlreadyExistsError(edge: Edge<Int>) : Error("edge from ${edge.from} to ${edge.to} already exists") {
        override fun toHttpResponse(path: String?): ResponseEntity<ErrorResponse> {
            return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ErrorResponse(message, path = path))
        }
    }

    class NotFoundEdgeError(edge: Edge<Int>) : Error("edge from ${edge.from} to ${edge.to} not found") {
        override fun toHttpResponse(path: String?): ResponseEntity<ErrorResponse> {
            return ResponseEntity.notFound().build()
        }
    }

    class NotFoundNodeError(node: Int) : Error("node $node not found") {
        override fun toHttpResponse(path: String?): ResponseEntity<ErrorResponse> {
            return ResponseEntity.notFound().build()
        }
    }

    class InvalidEdgeError(edge: Edge<Int>) : Error("cannot create edge from ${edge.from} to ${edge.to}") {
        override fun toHttpResponse(path: String?): ResponseEntity<ErrorResponse> {
            return ResponseEntity.badRequest().body(ErrorResponse(message, path = path))
        }
    }

    class DatabaseError(reason: String) : Error("database error: $reason") {
        override fun toHttpResponse(path: String?): ResponseEntity<ErrorResponse> {
            return ResponseEntity.internalServerError().body(ErrorResponse(message, path = path))
        }
    }
}