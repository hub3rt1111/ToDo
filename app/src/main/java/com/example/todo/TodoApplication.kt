package com.example.todo

import android.app.Application
import androidx.room.Room
import com.example.todo.data.local.AppDatabase
import com.example.todo.data.local.TaskRepository

class TodoApplication : Application() {
    private val database: AppDatabase by lazy {
        Room.databaseBuilder(this, AppDatabase::class.java, "task_database")
            .build()
    }

    val repository: TaskRepository by lazy {
        TaskRepository(database.taskDao())
    }
}