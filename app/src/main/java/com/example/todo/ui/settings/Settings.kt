package com.example.todo.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todo.ui.ToDoViewModelFactory


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Settings(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel
) {
    val timeNotificationsEnabled by viewModel.timeNotificationsEnabled.collectAsState()
    val locationNotificationsEnabled by viewModel.locationNotificationsEnabled.collectAsState()
    val themeMode by viewModel.themeMode.collectAsState()
    val vibrationEnabled by viewModel.vibrationEnabled.collectAsState()

    var showThemeDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") }
            )
        },
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            SettingsCategoryTitle(title = "Notifications")
            
            SettingsSwitchRow(
                icon = Icons.Default.NotificationsActive,
                title = "Time Notifications",
                subtitle = "Receive reminders about task deadlines",
                checked = timeNotificationsEnabled,
                onCheckedChange = { viewModel.setTimeNotificationsEnabled(it) }
            )
            
            SettingsSwitchRow(
                icon = Icons.Default.LocationOn,
                title = "Location Notifications",
                subtitle = "Receive reminders when arriving at a location",
                checked = locationNotificationsEnabled,
                onCheckedChange = { viewModel.setLocationNotificationsEnabled(it) }
            )

            SettingsSwitchRow(
                icon = Icons.Default.Vibration,
                title = "Vibration",
                subtitle = "Vibrate on notifications",
                checked = vibrationEnabled,
                onCheckedChange = { viewModel.setVibrationEnabled(it) }
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            
            SettingsCategoryTitle(title = "Appearance")

            SettingsClickableRow(
                icon = Icons.Default.Palette,
                title = "App Theme",
                subtitle = when (themeMode) {
                    "LIGHT" -> "Light"
                    "DARK" -> "Dark"
                    else -> "System"
                },
                onClick = { showThemeDialog = true }
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            SettingsCategoryTitle(title = "Information")
            
            SettingsClickableRow(
                icon = Icons.Default.Info,
                title = "About App",
                subtitle = "Task Manager v1.0",
                onClick = {}
            )
        }
    }

    if (showThemeDialog) {
        AlertDialog(
            onDismissRequest = { showThemeDialog = false },
            title = { Text("Select Theme") },
            text = {
                Column {
                    ThemeOption("System", "SYSTEM", themeMode) {
                        viewModel.setThemeMode("SYSTEM")
                        showThemeDialog = false
                    }
                    ThemeOption("Light", "LIGHT", themeMode) {
                        viewModel.setThemeMode("LIGHT")
                        showThemeDialog = false
                    }
                    ThemeOption("Dark", "DARK", themeMode) {
                        viewModel.setThemeMode("DARK")
                        showThemeDialog = false
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showThemeDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun SettingsCategoryTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
    )
}

@Composable
fun SettingsSwitchRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
fun SettingsClickableRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun ThemeOption(
    label: String,
    value: String,
    currentValue: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = (value == currentValue),
            onClick = onClick
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
    }
}
