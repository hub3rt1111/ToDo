package com.example.todo.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todo.data.local.AttachmentRepository
import com.example.todo.data.local.TaskRepository
import com.example.todo.ui.addtask.AddTaskViewModel
import com.example.todo.ui.home.HomeViewModel
import com.example.todo.ui.settings.SettingsViewModel
import com.example.todo.ui.taskdetails.TaskDetailsViewModel

class ToDoViewModelFactory(
    private val taskRepository: TaskRepository,
    private val attachmentRepository: AttachmentRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddTaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddTaskViewModel(taskRepository, attachmentRepository) as T
        }
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(taskRepository, attachmentRepository) as T
        }
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel(taskRepository) as T
        }
        if (modelClass.isAssignableFrom(TaskDetailsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskDetailsViewModel(taskRepository, attachmentRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}