package com.example.todo.ui.addtask

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimePicker
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.todo.data.local.Priority
import com.example.todo.data.local.RecurrenceType
import com.example.todo.ui.components.RepetitionAlertDialog
import com.example.todo.ui.components.SaveTaskButton
import com.example.todo.ui.theme.PriorityHigh
import com.example.todo.util.LocationUtils
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority as LocationPriority
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@Composable
fun AddTask(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    viewModel: AddTaskViewModel,
) {
    val context = LocalContext.current
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf<Long?>(null) }
    var priority by remember { mutableStateOf(Priority.LOW) }
    var category by remember { mutableStateOf("") }
    var attachments by remember { mutableStateOf<List<Uri>>(emptyList()) }
    var latitude by remember { mutableStateOf<Double?>(null) }
    var longitude by remember { mutableStateOf<Double?>(null) }
    var recurrenceType by remember { mutableStateOf(RecurrenceType.NONE) }
    var recurrenceInterval by remember { mutableStateOf(1) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        ) {
            LocationUtils.getCurrentLocation(context) { lat, lon ->
                latitude = lat
                longitude = lon
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {

        TopPanel(
            title = "Add task",
            onBackClick = onBackClick,
            onSaveClick = {
                viewModel.saveTaskWithAttachments(
                    title = title,
                    description = description,
                    priority = priority,
                    category = category,
                    dueDate = dueDate,
                    latitude = latitude,
                    longitude = longitude,
                    attachments = attachments,
                    recurrenceType = recurrenceType,
                    recurrenceInterval = recurrenceInterval
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
            latitude = latitude,
            longitude = longitude,
            attachments = attachments,
            recurrenceType = recurrenceType,
            recurrenceInterval = recurrenceInterval,
            onDueDateChange = { dueDate = it },
            onTitleChange = { title = it },
            onDescriptionChange = { description = it },
            onPriorityChange = { priority = it },
            onCategoryChange = { category = it },
            onAttachmentsChange = { attachments = it },
            onRecurrenceChange = {type, interval ->
                recurrenceType = type
                recurrenceInterval = interval
            },
            onLocationClick = {
                if (LocationUtils.hasLocationPermission(context)) {
                    LocationUtils.getCurrentLocation(context) { lat, lon ->
                        latitude = lat
                        longitude = lon
                    }
                } else {
                    permissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }
            }
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
    latitude: Double?,
    longitude: Double?,
    attachments: List<Uri>,
    recurrenceType: RecurrenceType,
    recurrenceInterval: Int,
    onDueDateChange: (Long?) -> Unit,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onPriorityChange: (Priority) -> Unit,
    onCategoryChange: (String) -> Unit,
    onAttachmentsChange: (List<Uri>) -> Unit,
    onRecurrenceChange: (RecurrenceType, Int) -> Unit,
    onLocationClick: () -> Unit,
) {
    var showRepetitionDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                .padding(8.dp)
        ) {
            TextField(
                value = title,
                onValueChange = { onTitleChange(it) },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                )
            )
            TextField(
                value = description,
                onValueChange = { onDescriptionChange(it) },
                label = { Text("Add task description") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                )
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            MyDateTimePicker(
                selectedDateTime = dueDate,
                onDateTimeSelected = onDueDateChange
            )
            val locationText = if (latitude != null && longitude != null) {
                "%.2f, %.2f".format(latitude, longitude)
            } else {
                "Select current"
            }
            InfoPanel(
                Icons.Default.LocationOn,
                "Location",
                locationText
            ) {
                onLocationClick()
            }

            val recurrenceText = when (recurrenceType) {
                RecurrenceType.NONE -> "None"
                RecurrenceType.DAILY -> "Daily"
                RecurrenceType.WEEKLY -> "Weekly"
                RecurrenceType.MONTHLY -> "Monthly"
                RecurrenceType.CUSTOM -> "Every $recurrenceInterval days"
            }

            InfoPanel(
                Icons.Default.Repeat,
                "Repetition",
                recurrenceText
            ) {
                showRepetitionDialog = true
            }
        }

        if (showRepetitionDialog) {
            RepetitionAlertDialog(
                onDismissRequest = { showRepetitionDialog = false },
                onConfirm = { type, interval ->
                    onRecurrenceChange(type, interval)
                },
                initialType = recurrenceType,
                initialInterval = recurrenceInterval
            )
        }

        PriorityPanel(
            selectedPriority = priority,
            onPrioritySelected = onPriorityChange,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                .padding(16.dp)
        )

        CategoryPanel(
            selectedText = category,
            onValueChange = onCategoryChange,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
        )

        AttachmentPanel(
            attachments = attachments,
            onAttachmentsChange = onAttachmentsChange,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                .padding(16.dp)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun InfoPanel(
    icon: ImageVector,
    description: String,
    info: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = "info image",
                modifier = Modifier.size(28.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.width(8.dp))
            Text(description)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                info,
                modifier = Modifier.widthIn(max = 150.dp),
                maxLines = 1
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = "select",
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun TopPanel(
    title: String,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    showDelete: Boolean = false,
    onDeleteClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "arrow back"
                )
            }
            Spacer(Modifier.width(16.dp))
            Text(
                title,
                style = MaterialTheme.typography.headlineMedium
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            if (showDelete) {
                IconButton(onClick = onDeleteClick) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "delete task",
                        tint = PriorityHigh
                    )
                }
                Spacer(Modifier.width(4.dp))
                VerticalDivider(
                    modifier = Modifier.height(24.dp),
                    thickness = 1.dp,
                    color = Color.LightGray
                )
                Spacer(Modifier.width(12.dp))
            }
            SaveTaskButton("Save") {
                onSaveClick()
            }
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
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        TextField(
            value = selectedText,
            onValueChange = {},
            readOnly = true,
            label = { Text("Select category") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(
                    type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                    enabled = true
                ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDateTimePicker(
    selectedDateTime: Long?,
    onDateTimeSelected: (Long) -> Unit,
) {
    val datePickerState = rememberDatePickerState()
    val timePickerState = rememberTimePickerState()
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    val formattedText = selectedDateTime?.let {
        LocalDateTime.ofInstant(Instant.ofEpochMilli(it), ZoneId.systemDefault())
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
    } ?: "Select date & time"

    InfoPanel(
        icon = Icons.Default.CalendarMonth,
        description = "Completion date",
        info = formattedText,
        onClick = { showDatePicker = true }
    )

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDatePicker = false
                        showTimePicker = true
                    }
                ) { Text("Next") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showTimePicker) {
        TimePickerDialog(
            onDismiss = { showTimePicker = false },
            onConfirm = {
                val dateMillis = datePickerState.selectedDateMillis ?: System.currentTimeMillis()
                val date = Instant.ofEpochMilli(dateMillis)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                
                val combinedDateTime = date.atTime(timePickerState.hour, timePickerState.minute)
                    .atZone(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli()
                
                onDateTimeSelected(combinedDateTime)
                showTimePicker = false
            }
        ) {
            TimePicker(state = timePickerState)
        }
    }
}

@Composable
fun TimePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit
) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onConfirm) { Text("OK") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                content()
            }
        }
    )
}

@Composable
fun AttachmentPanel(
    attachments: List<Uri>,
    onAttachmentsChange: (List<Uri>) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments(),
    ) { uris ->
        uris.forEach { uri ->
            try {
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        onAttachmentsChange(attachments + uris)
    }

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Attachments", fontSize = 18.sp)
            IconButton(onClick = { launcher.launch(arrayOf("*/*")) }) {
                Icon(Icons.Default.AttachFile, contentDescription = "Add attachment")
            }
        }

        if (attachments.isNotEmpty()) {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(attachments) { uri ->
                    AttachmentItem(uri = uri, onRemove = {
                        onAttachmentsChange(attachments.filter { it != uri })
                    })
                }
            }
        } else {
            Text("No attachments", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun AttachmentItem(
    uri: Uri,
    onRemove: () -> Unit
) {
    val context = LocalContext.current
    val mimeType = context.contentResolver.getType(uri)
    
    val icon = when {
        mimeType?.startsWith("image/") == true -> Icons.Default.Image
        mimeType?.startsWith("audio/") == true -> Icons.Default.Mic
        mimeType == "application/pdf" -> Icons.Default.Description
        else -> Icons.Default.AttachFile
    }

    Column(
        modifier = Modifier
            .size(80.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .clickable {
                try {
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        setDataAndType(uri, mimeType ?: "*/*")
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                    context.startActivity(intent)
                } catch (e: Exception) {
                    // Handle exception (e.g., no app to open the file)
                }
            }
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Icon(
                Icons.Default.Close,
                contentDescription = "Remove",
                modifier = Modifier
                    .size(16.dp)
                    .clickable { onRemove() }
            )
        }
        Icon(icon, contentDescription = null, modifier = Modifier.size(32.dp))
        Text(
            text = uri.lastPathSegment ?: "File",
            style = MaterialTheme.typography.labelSmall,
            maxLines = 1
        )
    }
}
