package com.example.todo

import android.app.Application
import androidx.room.Room
import com.example.todo.data.local.AppDatabase
import com.example.todo.data.local.AttachmentRepository
import com.example.todo.data.local.TaskRepository

class TodoApplication : Application() {
    private val database: AppDatabase by lazy {
        Room.databaseBuilder(this, AppDatabase::class.java, "task_database")
            .fallbackToDestructiveMigration()
            .build()
    }

    val taskRepository: TaskRepository by lazy {
        TaskRepository(database.taskDao())
    }

    val attachmentRepository: AttachmentRepository by lazy {
        AttachmentRepository(database.attachmentDao())
    }
}