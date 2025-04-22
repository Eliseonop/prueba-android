package com.tcontur.login_tcontur.ui.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.tcontur.login_tcontur.ui.data.db.entity.TicketEntity

@Dao
interface TicketDao {
    @Insert
    suspend fun insert(ticket: TicketEntity): Long // Devuelve el correlative generado como Long

    @Query("SELECT * FROM tickets WHERE correlative = :correlative")
    suspend fun getByCorrelative(correlative: Int): TicketEntity

    @Query("SELECT * FROM tickets")
    suspend fun getAll(): List<TicketEntity>

    @Query("SELECT * FROM tickets WHERE correlative IN (:correlatives)")
    suspend fun getTicketsByCorrelatives(correlatives: List<Long>): List<TicketEntity>

    @Query("SELECT COUNT(*) FROM tickets WHERE fare = :fareId")
    suspend fun getCountByFare(fareId: Int): Int?
}
