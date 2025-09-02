package com.example.prew.repository

import com.example.prew.dto.Edge
import com.example.prew.dto.Node
import com.example.prew.errors.Error
import com.example.prew.jooq.Tables.EDGES
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
class Repository(private val dsl: DSLContext) {
    fun insertEdge(edge: Edge<Int>): Result<Unit> {
        return try {
            val inserted = dsl.insertInto(EDGES)
                .set(EDGES.FROM_ID, edge.from)
                .set(EDGES.TO_ID, edge.to)
                .onConflictDoNothing()
                .execute()
            return if (inserted > 0) Result.success(Unit)
            else Result.failure(Error.AlreadyExistsError(edge))
        } catch (e: Exception) {
            Result.failure(Error.DatabaseError(e.message?: "unknown"))
        }
    }

    fun deleteEdge(edge: Edge<Int>): Result<Unit> {
        return try {
            val deleted = dsl.deleteFrom(EDGES)
                .where(EDGES.FROM_ID.eq(edge.from))
                .and(EDGES.TO_ID.eq(edge.to))
                .execute()
            if (deleted > 0) Result.success(Unit)
            else Result.failure(Error.NotFoundEdgeError(edge))
        } catch (e: Exception) {
            Result.failure(Error.DatabaseError(e.message ?: "unknown"))
        }
    }

    private fun getEdges(id: Int): List<Edge<Int>> {
        return dsl.select(EDGES.FROM_ID, EDGES.TO_ID)
            .from(EDGES)
            .where(EDGES.FROM_ID.eq(id))
            .fetch()
            .map { Edge(it.value1(), it.value2()) }
    }

    private fun getEdgesByDestination(id: Int): List<Edge<Int>> {
        return dsl.select(EDGES.FROM_ID, EDGES.TO_ID)
            .from(EDGES)
            .where(EDGES.TO_ID.eq(id))
            .fetch()
            .map { Edge(it.value1(), it.value2()) }
    }

    private fun buildTree(
        id: Int,
        maxDepth: Int = 10,
        visited: MutableSet<Int> = mutableSetOf()
    ): Node<Int> {
        if (maxDepth < 0) {
            return Node(id, emptyList())
        }
        if (!visited.add(id)) {
            return Node(id, emptyList())
        }

        val edges = getEdges(id)
        if (edges.isEmpty()) {
            return Node(id, emptyList())
        }
        val children = edges.map { edge -> buildTree(edge.to, maxDepth - 1, visited) }
        return Node(id, children)
    }

    fun getTree(id: Int): Result<Node<Int>> {
        return try {
            val tree = buildTree(id)
            val edges = getEdgesByDestination(id)
            if (tree.children.isEmpty() && edges.isEmpty()) {
                return Result.failure(Error.NotFoundNodeError(id))
            }
            return Result.success(tree)
        } catch (e: Exception) {
            Result.failure(Error.DatabaseError(e.message ?: "unknown"))
        }
    }
}