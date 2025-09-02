package com.example.prew.service

import com.example.prew.dto.Edge
import com.example.prew.errors.Error
import com.example.prew.repository.EdgeRepository
import org.springframework.stereotype.Service

@Service
class TreeService(private val repository: EdgeRepository) {
    fun createEdge(edge: Edge<Int>): Result<Unit> {
        if (edge.from == edge.to) {
            return Result.failure(Error.InvalidEdgeError(edge))
        }
        return repository.insertEdge(edge)
    }

    fun deleteEdge(edge: Edge<Int>): Result<Unit> {
        return repository.deleteEdge(edge)
    }
}