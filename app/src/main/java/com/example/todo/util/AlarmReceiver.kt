package com.example.todo.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val taskId = intent.getLongExtra("taskId", -1L)
        val title = intent.getStringExtra("title") ?: "Task Reminder"
        val description = intent.getStringExtra("description") ?: "You have a task coming up soon."

        // alarm przyniósł prawidłowe dane
        if (taskId != -1L) {
            NotificationHelper.showNotification(
                context,
                title,
                description,
                taskId
            )
        }
    }
}
