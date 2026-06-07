package com.example.todo.ui.addtask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todo.data.local.Priority
import com.example.todo.data.local.TaskEntity
import com.example.todo.data.local.TaskRepository
import com.example.todo.data.local.TaskStatus
import kotlinx.coroutines.launch

class AddTaskViewModel(private val repository: TaskRepository) : ViewModel() {

    fun saveTask(title: String, description: String, priority: Priority, category: String) {
        viewModelScope.launch {
            val newTask = TaskEntity(
                title = title,
                description = description,
                createdAt = System.currentTimeMillis(),
                priority = priority,
                category = category,
                status = TaskStatus.TODO,
                dueDate = null, latitude = null, longitude = null, reminderTime = null
            )
            repository.addTask(newTask)
        }
    }
}