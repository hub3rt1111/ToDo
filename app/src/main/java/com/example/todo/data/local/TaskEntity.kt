package com.example.todo.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val createdAt: Long,
    val dueDate: Long?,
    val status: TaskStatus,
    val priority: Priority,
    val category: String?,
    val latitude: Double?,
    val longitude: Double?,
    val reminderTime: Long?
)