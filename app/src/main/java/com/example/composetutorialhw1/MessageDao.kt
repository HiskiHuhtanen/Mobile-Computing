package com.example.composetutorialhw1

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Query("SELECT * FROM MessageData")
    suspend fun getAll(): List<MessageData>

    //https://developer.android.com/kotlin/flow
    //Gotta make it flow to get live updates
    @Query("SELECT * FROM MessageData")
    fun getAllUpdates(): Flow<List<MessageData>>

    @Insert
    suspend fun insertMessage(vararg message: MessageData)

    @Delete
    suspend fun delete(message: MessageData)
}