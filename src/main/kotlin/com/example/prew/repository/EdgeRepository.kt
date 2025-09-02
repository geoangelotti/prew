package com.example.prew.repository

import com.example.prew.dto.Edge
import com.example.prew.errors.Error
import com.example.prew.jooq.Tables.EDGES
import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
class EdgeRepository(private val dsl: DSLContext) {
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
            else Result.failure(Error.NotFoundError(edge))
        } catch (e: Exception) {
            Result.failure(Error.DatabaseError(e.message ?: "unknown"))
        }
    }
}