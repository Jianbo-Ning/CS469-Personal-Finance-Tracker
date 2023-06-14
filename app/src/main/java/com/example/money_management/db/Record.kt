package com.example.money_management.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "record")
data class Record(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    val type: String,
    val spendType: String,
    val money: Double,
    val date: Date,
    val note: String
)
