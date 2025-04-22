package com.tcontur.login_tcontur.ui.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tcontur.login_tcontur.ui.data.db.entity.BoletoCountEntity

@Dao
interface BoletoCountDao {
    @Query("SELECT * FROM boleto_counters WHERE fare = :fare")
    suspend fun getCounter(fare: Int): BoletoCountEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCounter(counter: BoletoCountEntity)

    @Query("SELECT * FROM boleto_counters")
    suspend fun getAllCounters(): List<BoletoCountEntity>
}