package com.example.prew.service

import com.example.prew.dto.Edge
import com.example.prew.dto.Node
import com.example.prew.errors.Error
import com.example.prew.repository.Repository
import org.springframework.stereotype.Service

@Service
class Service(private val repository: Repository) {
    fun createEdge(edge: Edge<Int>): Result<Unit> {
        if (edge.from == edge.to) {
            return Result.failure(Error.InvalidEdgeError(edge))
        }
        return repository.insertEdge(edge)
    }

    fun deleteEdge(edge: Edge<Int>): Result<Unit> {
        return repository.deleteEdge(edge)
    }

    fun getTree(id: Int): Result<Node<Int>> {
        return repository.getTree(id)
    }
}