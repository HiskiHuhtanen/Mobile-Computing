package com.example.composetutorialhw1

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
@Entity
data class MessageData(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name = "author") val author: String?,
    @ColumnInfo(name = "text") val text: String?
)