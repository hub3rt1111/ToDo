package com.example.todo.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AttachmentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val taskId: Long,
    val uri: String
)