package com.example.todo.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [TaskEntity::class, AttachmentEntity::class, LocationEntity::class],
    version = 4
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun attachmentDao(): AttachmentDao
    abstract fun locationDao(): LocationDao
}