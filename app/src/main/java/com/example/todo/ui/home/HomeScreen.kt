package com.example.todo.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.todo.data.local.TaskStatus
import com.example.todo.ui.ToDoViewModelFactory
import com.example.todo.ui.addtask.AddTask
import com.example.todo.ui.addtask.AddTaskViewModel
import com.example.todo.ui.components.TaskItem
import com.example.todo.ui.components.ViewButton
import com.example.todo.ui.components.getLabel
import com.example.todo.ui.settings.Settings
import com.example.todo.ui.settings.SettingsViewModel
import com.example.todo.ui.taskdetails.TaskDetails
import com.example.todo.ui.taskdetails.TaskDetailsViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun MainScreen(factory: ToDoViewModelFactory) {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { BottomBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") {
                val homeViewModel: HomeViewModel = viewModel(factory = factory)
                HomeScreen(Modifier, homeViewModel, navController)
            }
            composable("addTask") {
                val addTaskViewModel: AddTaskViewModel = viewModel(factory = factory)
                AddTask(
                    Modifier,
                    onBackClick = {navController.popBackStack()},
                    viewModel = addTaskViewModel,
                )
            }
            composable("settings") {
                val settingsViewModel: SettingsViewModel = viewModel(factory = factory)
                Settings(viewModel = settingsViewModel)
            }
            composable("taskDetails/{taskId}") { backStackEntry ->
                val taskId = backStackEntry.arguments?.getString("taskId")?.toLong()
                val taskDetailsViewModel: TaskDetailsViewModel = viewModel(factory = factory)
                TaskDetails(
                    taskId = taskId,
                    viewModel = taskDetailsViewModel,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel,
    navController: NavController
) {
    val today = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern(
        "EEEE, d MMMM, yyyy",
        Locale.ENGLISH
    )

    val tasks by homeViewModel.tasks.collectAsState()
    var query by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All") }
    var selectedSort by remember { mutableStateOf("Default") }

    val filteredTasks = remember(tasks, query, selectedFilter, selectedSort) {
        tasks.filter { task ->
            val matchesQuery = task.title.contains(query, ignoreCase = true)
            val matchesFilter = when (selectedFilter) {
                "For today" -> getLabel(task.dueDate) == "Today"
                "Outstanding" -> task.status == TaskStatus.TODO
                else -> true
            }
            matchesQuery && matchesFilter
        }.let { filtered ->
            when (selectedSort) {
                "Priority" -> filtered.sortedByDescending { it.priority }
                "Due Date" -> filtered.sortedBy { it.dueDate ?: Long.MAX_VALUE }
                else -> filtered
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text(
                    "My tasks",
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    today.format(formatter),
                    style = MaterialTheme.typography.bodyMedium,
                    fontStyle = FontStyle.Italic,
                    color = Color.Gray,
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    .padding(8.dp)
            ) {
                SearchBar(
                    query = query,
                    onQueryChange = { query = it })
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    .padding(12.dp)
            ) {
                Text(
                    "Filter by status",
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp),
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    ViewButton(
                        Modifier.weight(1f), "All", selectedFilter == "All"
                    ) {
                        selectedFilter = "All"
                    }
                    ViewButton(
                        Modifier.weight(1f), "Today", selectedFilter == "For today"
                    ) {
                        selectedFilter = "For today"
                    }
                    ViewButton(
                        Modifier.weight(1f), "Pending", selectedFilter == "Outstanding"
                    ) {
                        selectedFilter = "Outstanding"
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    "Sort by",
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp),
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    ViewButton(
                        Modifier.weight(1f), "Default", selectedSort == "Default"
                    ) {
                        selectedSort = "Default"
                    }
                    ViewButton(
                        Modifier.weight(1f), "Priority", selectedSort == "Priority"
                    ) {
                        selectedSort = "Priority"
                    }
                    ViewButton(
                        Modifier.weight(1f), "Due Date", selectedSort == "Due Date"
                    ) {
                        selectedSort = "Due Date"
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filteredTasks) { task ->
                val attachmentCount by homeViewModel.getAttachmentsCountByTaskId(task.id).collectAsState(initial = 0)
                TaskItem(
                    task = task,
                    attachmentCount = attachmentCount,
                    isDone = task.status == TaskStatus.DONE,
                    onClick = {
                        navController.navigate("taskDetails/${task.id}")
                    }) { checked ->
                    homeViewModel.updateTask(task.copy(status = if (checked) TaskStatus.DONE else TaskStatus.TODO))
                }
            }
        }
    }
}

@Composable
fun BottomBar(navController: NavController) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar {
        NavigationBarItem(
            selected = currentRoute == "home",
            onClick = { navController.navigate("home") },
            icon = { Icon(imageVector = Icons.Default.Home, contentDescription = "home") },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = currentRoute == "addTask",
            onClick = { navController.navigate("addTask") },
            icon = { Icon(imageVector = Icons.Default.AddCircle, contentDescription = "addTask") },
            label = { Text("Add Task") }
        )
        NavigationBarItem(
            selected = currentRoute == "settings",
            onClick = { navController.navigate("settings") },
            icon = { Icon(imageVector = Icons.Default.Settings, contentDescription = "settings") },
            label = { Text("Settings") }
        )
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        label = { Text("Search tasks...") },
        modifier = Modifier
            .fillMaxWidth(),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
        )
    )
}