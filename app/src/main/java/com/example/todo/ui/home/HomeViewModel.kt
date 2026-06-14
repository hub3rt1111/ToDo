package com.example.todo.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.data.local.AttachmentRepository
import com.example.todo.data.local.TaskEntity
import com.example.todo.data.local.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val taskRepository: TaskRepository,
    private val attachmentRepository: AttachmentRepository
) : ViewModel() {
    val tasks = taskRepository.getTasks().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun addTask(task: TaskEntity) {
        viewModelScope.launch {
            taskRepository.addTask(task)
        }
    }

    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch {
            taskRepository.deleteTask(task)
        }
    }

    fun updateTask(task: TaskEntity) {
        viewModelScope.launch {
            taskRepository.updateTask(task)
        }
    }

    fun getAttachmentsCountByTaskId(taskId: Long): Flow<Int> {
        return attachmentRepository.getAttachmentsCountForTask(taskId)
    }
}
