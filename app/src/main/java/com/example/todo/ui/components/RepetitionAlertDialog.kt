package com.example.todo.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.example.todo.data.local.RecurrenceType

@Composable
fun RepetitionAlertDialog(
    onDismissRequest: () -> Unit,
    onConfirm: (RecurrenceType, Int) -> Unit,
    initialType: RecurrenceType = RecurrenceType.NONE,
    initialInterval: Int = 1
) {
    var selectedType by remember { mutableStateOf(initialType) }
    var interval by remember { mutableStateOf(initialInterval.toString()) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text("Select Recurrence") },
        text = {
            Column(Modifier.selectableGroup()) {
                RecurrenceOption(
                    label = "None",
                    selected = selectedType == RecurrenceType.NONE,
                    onClick = { selectedType = RecurrenceType.NONE }
                )
                RecurrenceOption(
                    label = "Daily",
                    selected = selectedType == RecurrenceType.DAILY,
                    onClick = { selectedType = RecurrenceType.DAILY }
                )
                RecurrenceOption(
                    label = "Weekly",
                    selected = selectedType == RecurrenceType.WEEKLY,
                    onClick = { selectedType = RecurrenceType.WEEKLY }
                )
                RecurrenceOption(
                    label = "Monthly",
                    selected = selectedType == RecurrenceType.MONTHLY,
                    onClick = { selectedType = RecurrenceType.MONTHLY }
                )
                RecurrenceOption(
                    label = "Custom (days)",
                    selected = selectedType == RecurrenceType.CUSTOM,
                    onClick = { selectedType = RecurrenceType.CUSTOM }
                )
                
                if (selectedType == RecurrenceType.CUSTOM) {
                    OutlinedTextField(
                        value = interval,
                        onValueChange = { if (it.all { char -> char.isDigit() }) interval = it },
                        label = { Text("Interval (days)") },
                        modifier = Modifier.padding(top = 8.dp).fillMaxWidth()
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                onConfirm(selectedType, interval.toIntOrNull() ?: 1)
                onDismissRequest()
            }) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text("Cancel")
            }
        }
    )
}

@Composable
private fun RecurrenceOption(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(48.dp)
            .selectable(
                selected = selected,
                onClick = onClick,
                role = Role.RadioButton
            )
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = null
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}
