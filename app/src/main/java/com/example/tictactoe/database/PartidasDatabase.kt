package com.example.tictactoe.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tictactoe.R
import kotlinx.coroutines.CoroutineScope

@Database(entities = [Partida::class], version = 1, exportSchema = false)
abstract  class PartidasDatabase : RoomDatabase() {
    abstract fun partidaDao(): PartidasDAO

    companion object {
        @Volatile
        internal var INSTANCE: PartidasDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): PartidasDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PartidasDatabase::class.java,
                    context.getString(R.string.tictactoe_database)
                )
                    .fallbackToDestructiveMigration()
                    //.addCallback(PartidasDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}