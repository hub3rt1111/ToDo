package com.example.todo.ui.addtask

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.data.local.AttachmentEntity
import com.example.todo.data.local.AttachmentRepository
import com.example.todo.data.local.Priority
import com.example.todo.data.local.RecurrenceType
import com.example.todo.data.local.TaskEntity
import com.example.todo.data.local.TaskRepository
import com.example.todo.data.local.TaskStatus
import com.example.todo.util.TaskReminderManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class AddTaskViewModel(
    private val taskRepository: TaskRepository,
    private val attachmentRepository: AttachmentRepository,
    private val taskReminderManager: TaskReminderManager
) : ViewModel() {

    fun saveTaskWithAttachments(
        title: String,
        description: String,
        priority: Priority,
        category: String,
        dueDate: Long?,
        latitude: Double?,
        longitude: Double?,
        attachments: List<Uri>,
        recurrenceType: RecurrenceType = RecurrenceType.NONE,
        recurrenceInterval: Int = 1
    ) {
        viewModelScope.launch {
            val task = TaskEntity(
                title = title,
                description = description,
                createdAt = System.currentTimeMillis(),
                priority = priority,
                category = category,
                status = TaskStatus.TODO,
                dueDate = dueDate,
                latitude = latitude,
                longitude = longitude,
                reminderTime = null,
                recurrenceType = recurrenceType,
                recurrenceInterval = recurrenceInterval
            )
            val taskId = taskRepository.addTask(task)
            val savedTask = task.copy(id = taskId)
            
            attachments.forEach { uri ->
                attachmentRepository.insert(
                    AttachmentEntity(taskId = taskId, uri = uri.toString())
                )
            }
            
            // Schedule reminders for the new task
            taskReminderManager.scheduleTaskReminders(savedTask)
        }
    }

    fun getAttachmentsCountByTaskId(id: Long): Flow<Int> {
        return attachmentRepository.getAttachmentsCountForTask(id)
    }
}
