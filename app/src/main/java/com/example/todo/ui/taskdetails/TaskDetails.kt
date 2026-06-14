package com.example.todo.ui.taskdetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.todo.ui.addtask.TopPanel

@Composable
fun TaskDetails(modifier: Modifier = Modifier, taskId: Long?) {
    if (taskId == null) return

    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .background(MaterialTheme.colorScheme.background)
    ) {
        TopPanel(
            title = "Task details",
            onBackClick = {},
            onSaveClick = {}
        )
    }
}