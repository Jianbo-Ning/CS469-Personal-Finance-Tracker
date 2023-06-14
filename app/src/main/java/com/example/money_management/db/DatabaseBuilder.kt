package com.example.money_management.db

import android.content.Context
import androidx.room.Room

object DatabaseBuilder {
    private var instance: AppDatabase? = null

    fun getInstance(context: Context): AppDatabase {
        if (instance == null) {
            synchronized(AppDatabase::class) {
                instance = buildDatabase(context)
            }
        }
        return instance!!
    }

    private fun buildDatabase(context: Context) =
        Room.databaseBuilder(context, AppDatabase::class.java, "app-database.db")
            .fallbackToDestructiveMigration()
            .build()
}
