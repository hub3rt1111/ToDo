package com.example.todo.ui.taskdetails

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TaskDetails(modifier: Modifier = Modifier, taskId: Long?) {
    if(taskId == null) return

    Text(taskId.toString())
}