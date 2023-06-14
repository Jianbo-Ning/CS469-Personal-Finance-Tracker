package com.example.money_management.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete

@Dao
interface RecordDao {
    @Insert
     fun addRecord(record: Record)

    @Query("SELECT * FROM record")
     fun getAllRecords(): List<Record>

    @Update
     fun updateRecord(record: Record)

    @Delete
     fun deleteRecord(record: Record)

     @Query("select sum(money) from record")
     fun getTotal(): Double;

    @Query("select * from record where id= :recordId")
    fun getRecordById(recordId: Int): Record
}
