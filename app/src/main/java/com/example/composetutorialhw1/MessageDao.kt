package com.example.composetutorialhw1

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MessageDao {
    @Query("SELECT * FROM MessageData")
    suspend fun getAll(): List<MessageData>

    @Insert
    suspend fun insertMessage(vararg message: MessageData)

    @Delete
    suspend fun delete(message: MessageData)
}