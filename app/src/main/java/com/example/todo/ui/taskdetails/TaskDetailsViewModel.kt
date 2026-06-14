package com.example.todo.ui.taskdetails

import androidx.lifecycle.ViewModel
import com.example.todo.data.local.AttachmentRepository
import com.example.todo.data.local.TaskRepository

class TaskDetailsViewModel(
    private val taskRepository: TaskRepository,
    private val attachmentRepository: AttachmentRepository
) : ViewModel() {


}
