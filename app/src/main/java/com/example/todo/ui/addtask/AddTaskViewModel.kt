package com.example.todo.ui.addtask

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.data.local.AttachmentEntity
import com.example.todo.data.local.AttachmentRepository
import com.example.todo.data.local.Priority
import com.example.todo.data.local.TaskEntity
import com.example.todo.data.local.TaskRepository
import com.example.todo.data.local.TaskStatus
import kotlinx.coroutines.launch

class AddTaskViewModel(
    private val taskRepository: TaskRepository,
    private val attachmentRepository: AttachmentRepository
) : ViewModel() {

    fun saveTaskWithAttachment(
        title: String,
        description: String,
        priority: Priority,
        category: String,
        dueDate: Long?,
        latitude: Double?,
        longitude: Double?,
        attachmentUri: Uri?
    ) {
        viewModelScope.launch {
            val taskId = taskRepository.addTask(
                TaskEntity(
                    title = title,
                    description = description,
                    createdAt = System.currentTimeMillis(),
                    priority = priority,
                    category = category,
                    status = TaskStatus.TODO,
                    dueDate = dueDate,
                    latitude = latitude,
                    longitude = longitude,
                    reminderTime = null
                )
            )
            attachmentUri?.let {
                attachmentRepository.insert(
                    AttachmentEntity(taskId = taskId, uri = it.toString())
                )
            }
        }
    }
}