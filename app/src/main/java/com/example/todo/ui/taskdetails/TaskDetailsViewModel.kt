package com.example.todo.ui.taskdetails

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.data.local.AttachmentEntity
import com.example.todo.data.local.AttachmentRepository
import com.example.todo.data.local.LocationEntity
import com.example.todo.data.local.LocationRepository
import com.example.todo.data.local.TaskEntity
import com.example.todo.data.local.TaskRepository
import com.example.todo.util.TaskReminderManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class TaskDetailsViewModel(
    private val taskRepository: TaskRepository,
    private val attachmentRepository: AttachmentRepository,
    private val locationRepository: LocationRepository,
    private val taskReminderManager: TaskReminderManager
) : ViewModel() {

    private val _task = MutableStateFlow<TaskEntity?>(null)
    val task: StateFlow<TaskEntity?> = _task.asStateFlow()

    private val _attachments = MutableStateFlow<List<AttachmentEntity>>(emptyList())
    val attachments: StateFlow<List<AttachmentEntity>> = _attachments.asStateFlow()

    private val _locations = MutableStateFlow<List<LocationEntity>>(emptyList())
    val locations: StateFlow<List<LocationEntity>> = _locations.asStateFlow()

    fun loadTask(taskId: Long) {
        viewModelScope.launch {
            _task.value = taskRepository.getTaskById(taskId)
            attachmentRepository.getAttachmentsForTask(taskId).collect {
                _attachments.value = it
            }
        }
        viewModelScope.launch {
            locationRepository.getLocationsForTask(taskId).collect {
                _locations.value = it
            }
        }
    }

    fun updateTask(task: TaskEntity) {
        viewModelScope.launch {
            taskRepository.updateTask(task)
            refreshReminders(task)
        }
    }

    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch {
            taskReminderManager.cancelReminders(task.id)
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

    fun addLocation(taskId: Long, latitude: Double, longitude: Double) {
        viewModelScope.launch {
            locationRepository.insert(
                LocationEntity(taskId = taskId, latitude = latitude, longitude = longitude)
            )
            task.value?.let { refreshReminders(it) }
        }
    }

    fun deleteLocation(location: LocationEntity) {
        viewModelScope.launch {
            locationRepository.delete(location)
            task.value?.let { refreshReminders(it) }
        }
    }

    private suspend fun refreshReminders(task: TaskEntity) {
        val currentLocations = locationRepository.getLocationsForTask(task.id).first()
        taskReminderManager.cancelReminders(task.id)
        taskReminderManager.scheduleTaskReminders(task, currentLocations)
    }
}
