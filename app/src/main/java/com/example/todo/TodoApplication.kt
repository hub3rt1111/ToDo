package com.example.todo

import android.app.Application
import androidx.room.Room
import com.example.todo.data.SettingsRepository
import com.example.todo.data.local.AppDatabase
import com.example.todo.data.local.AttachmentRepository
import com.example.todo.data.local.LocationRepository
import com.example.todo.data.local.TaskRepository
import com.example.todo.util.NotificationHelper
import com.example.todo.util.TaskReminderManager

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

    val locationRepository: LocationRepository by lazy {
        LocationRepository(database.locationDao())
    }

    val settingsRepository: SettingsRepository by lazy {
        SettingsRepository(this)
    }

    val taskReminderManager: TaskReminderManager by lazy {
        TaskReminderManager(this)
    }

    override fun onCreate() {
        super.onCreate()
        NotificationHelper.createNotificationChannel(this)
    }
}
