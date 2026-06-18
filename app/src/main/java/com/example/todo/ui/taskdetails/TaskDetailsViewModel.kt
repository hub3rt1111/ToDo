package com.example.todo.ui.taskdetails

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.data.local.AttachmentEntity
import com.example.todo.data.local.AttachmentRepository
import com.example.todo.data.local.TaskEntity
import com.example.todo.data.local.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TaskDetailsViewModel(
    private val taskRepository: TaskRepository,
    private val attachmentRepository: AttachmentRepository
) : ViewModel() {

    private val _task = MutableStateFlow<TaskEntity?>(null)
    val task: StateFlow<TaskEntity?> = _task.asStateFlow()

    private val _attachments = MutableStateFlow<List<AttachmentEntity>>(emptyList())
    val attachments: StateFlow<List<AttachmentEntity>> = _attachments.asStateFlow()

    fun loadTask(taskId: Long) {
        viewModelScope.launch {
            _task.value = taskRepository.getTaskById(taskId)
            attachmentRepository.getAttachmentsForTask(taskId).collect {
                _attachments.value = it
            }
        }
    }

    fun updateTask(task: TaskEntity) {
        viewModelScope.launch {
            taskRepository.updateTask(task)
        }
    }

    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch {
            taskRepository.deleteTask(task)
        }
    }

    fun addAttachment(taskId: Long, uri: Uri) {
        viewModelScope.launch {
            attachmentRepository.insert(
                AttachmentEntity(taskId = taskId, uri = uri.toString())
            )
        }
    }

    fun deleteAttachment(attachment: AttachmentEntity) {
        viewModelScope.launch {
            attachmentRepository.delete(attachment)
        }
    }

    fun getAttachmentsForTask(taskId: Long) =
        attachmentRepository.getAttachmentsForTask(taskId)

    fun getAttachmentsCountForTask(taskId: Long) =
        attachmentRepository.getAttachmentsCountForTask(taskId)

    fun updateAttachment(attachment: AttachmentEntity) {
        viewModelScope.launch {
            attachmentRepository.update(attachment)
        }
    }
}
