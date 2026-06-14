package com.example.todo.ui.taskdetails

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.todo.data.local.Priority
import com.example.todo.data.local.TaskStatus
import com.example.todo.ui.addtask.AddTaskViewModel
import com.example.todo.ui.addtask.TopPanel

@Composable
fun TaskDetails(
    modifier: Modifier = Modifier,
    taskId: Long?,
    onBackClick: () -> Unit,
    viewModel: TaskDetailsViewModel
) {
    if (taskId == null) return

    val context = LocalContext.current
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var status by remember {mutableStateOf(TaskStatus.TODO)}
    var createdAt by remember { mutableStateOf<Long?>(null) }
    var dueDate by remember { mutableStateOf<Long?>(null) }
    var priority by remember { mutableStateOf(Priority.LOW) }
    var category by remember { mutableStateOf("") }
    var attachment by remember { mutableStateOf<Uri?>(null) }
    var latitude by remember { mutableStateOf<Double?>(null) }
    var longitude by remember { mutableStateOf<Double?>(null) }
    var reminderTime by remember { mutableStateOf<Double?>(null) }

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