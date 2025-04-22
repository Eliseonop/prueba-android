package com.tcontur.login_tcontur.ui.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tcontur.login_tcontur.ui.data.db.dao.BoletoCountDao
import com.tcontur.login_tcontur.ui.data.db.dao.TicketDao
import com.tcontur.login_tcontur.ui.data.db.entity.BoletoCountEntity
import com.tcontur.login_tcontur.ui.data.db.entity.TicketEntity


@Database(
    entities = [
        TicketEntity::class,
        BoletoCountEntity::class
    ], version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun ticketDao(): TicketDao

    abstract fun boletoCountDao(): BoletoCountDao
}


