package com.example.prew.errors

import com.example.prew.dto.Edge

sealed class Error(override val message: String) : Exception(message) {
    class AlreadyExistsError(edge: Edge<Int>) : Error("edge from ${edge.from} to ${edge.to} already exists")
    class NotFoundEdgeError(edge: Edge<Int>) : Error("edge from ${edge.from} to ${edge.to} not found")
    class NotFoundNodeError(node: Int) : Error("node $node not found")
    class InvalidEdgeError(edge: Edge<Int>) : Error("cannot create edge from ${edge.from} to ${edge.to}")
    class DatabaseError(reason: String) : Error("database error: $reason")
}