package com.example.todo.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [TaskEntity::class, AttachmentEntity::class],
    version = 3
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun attachmentDao(): AttachmentDao
}