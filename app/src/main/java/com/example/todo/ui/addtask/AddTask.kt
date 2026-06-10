package com.example.todo.ui.addtask

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todo.data.local.Priority
import com.example.todo.ui.components.SaveTaskButton
import java.time.Instant
import java.time.ZoneId


@Composable

fun AddTask(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    viewModel: AddTaskViewModel,
) {

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf<Long?>(null) }
    var priority by remember { mutableStateOf(Priority.LOW) }
    var category by remember { mutableStateOf("") }
    var attachment by remember { mutableStateOf<Uri?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        TopPanel(
            onBackClick = onBackClick,
            onSaveClick = {
                viewModel.saveTaskWithAttachment(
                    title = title,
                    description = description,
                    priority = priority,
                    category = category,
                    attachmentUri = attachment
                )
                onBackClick()
            }
        )

        Spacer(Modifier.height(16.dp))

        TaskForm(
            title = title,
            description = description,
            priority = priority,
            category = category,
            dueDate = dueDate,
            onDueDateChange = { dueDate = it },
            onTitleChange = { title = it },
            onDescriptionChange = { description = it },
            onPriorityChange = { priority = it },
            onCategoryChange = { category = it },
            onAttachmentChange = { attachment = it }
        )

    }
}

@Composable
private fun TaskForm(
    title: String,
    description: String,
    priority: Priority,
    category: String,
    dueDate: Long?,
    onDueDateChange: (Long?) -> Unit,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onPriorityChange: (Priority) -> Unit,
    onCategoryChange: (String) -> Unit,
    onAttachmentChange: (Uri) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center
    ) {
        Column() {
            TextField(
                value = title,
                onValueChange = { onTitleChange(it) },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = description,
                onValueChange = { onDescriptionChange(it) },
                label = { Text("Add task description") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            MyDatePicker(
                selectedDate = dueDate,
                onDateSelected = onDueDateChange
            )
            InfoPanel(
                Icons.Default.LocationOn,
                "Location",
                "Select location"
            ) {

            }
            InfoPanel(
                Icons.Default.Repeat,
                "Repetition",
                "Select repetition"
            ) {

            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        PriorityPanel(
            selectedPriority = priority,
            onPrioritySelected = onPriorityChange
        )

        Spacer(modifier = Modifier.height(16.dp))

        CategoryPanel(
            selectedText = category,
            onValueChange = onCategoryChange
        )
        Spacer(modifier = Modifier.height(16.dp))

        AttachmentPicker(
            onFileSelected = { uri ->
                onAttachmentChange(uri)
            }
        )
    }
}

@Composable
private fun InfoPanel(
    icon: ImageVector,
    description: String,
    info: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row {
            Icon(
                imageVector = icon,
                contentDescription = "info image",
                modifier = Modifier.size(28.dp)
            )
            Text(description)
        }
        Row {
            Text(info)
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = "select",
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
private fun TopPanel(
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "arrow back",
                modifier = Modifier
                    .size(28.dp)
                    .clickable { onBackClick() }
            )
            Spacer(Modifier.width(8.dp))
            Text(
                "Add task",
                fontSize = 32.sp
            )
        }

        SaveTaskButton("Save") {
            onSaveClick()
        }
    }
}

@Composable
fun PriorityPanel(
    selectedPriority: Priority,
    onPrioritySelected: (Priority) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text("Priority", fontSize = 18.sp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .selectableGroup(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Priority.entries.forEach { priority ->
                Row(
                    Modifier
                        .selectable(
                            selected = (priority == selectedPriority),
                            onClick = { onPrioritySelected(priority) },
                            role = Role.RadioButton
                        )
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (priority == selectedPriority),
                        onClick = null
                    )
                    Text(
                        text = priority.name,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryPanel(
    selectedText: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    var expanded by remember { mutableStateOf(false) }
    val categoryItems = listOf("Work", "Personal", "Shopping", "Health", "Finance", "Other")
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            value = selectedText,
            onValueChange = {},
            readOnly = true,
            label = { Text("Select category") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier.menuAnchor(
                type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                enabled = true
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            categoryItems.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item) },
                    onClick = {
                        onValueChange(item)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun MyDatePicker(
    selectedDate: Long?,
    onDateSelected: (Long) -> Unit,
) {
    val datePickerState = rememberDatePickerState()
    var showDialog by remember { mutableStateOf(false) }

    val formattedText = selectedDate?.let {
        Instant.ofEpochMilli(it)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
            .toString()
    } ?: "Select date"

    InfoPanel(
        icon = Icons.Default.CalendarMonth,
        description = "Completion date",
        info = formattedText,
        onClick = { showDialog = true }
    )

    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            onDateSelected(it)
                        }
                        showDialog = false
                    }
                ) { Text("OK") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
fun AttachmentPicker(
    onFileSelected: (Uri) -> Unit
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
    ) { uri ->
        uri?.let { onFileSelected(it) }
    }
    Button(
        onClick = { launcher.launch("*/*") }
    ) {
        Text("Select file")
    }
}



