package com.example.todo.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import com.example.todo.data.local.TaskEntity
import com.example.todo.data.local.TaskStatus
import com.example.todo.ui.ToDoViewModelFactory
import com.example.todo.ui.addtask.AddTask
import com.example.todo.ui.addtask.AddTaskViewModel
import com.example.todo.ui.components.TaskItem
import com.example.todo.ui.components.ViewButton
import com.example.todo.ui.settings.Settings
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
                HomeScreen(Modifier.padding(innerPadding), homeViewModel)
            }
            composable("addTask") {
                val addTaskViewModel: AddTaskViewModel = viewModel(factory = factory)
                AddTask(
                    Modifier.padding(innerPadding),
                    onBackClick = {},
                    viewModel = addTaskViewModel
                )
            }
            composable("settings") { Settings() }
        }
    }
}

@Composable
fun HomeScreen(modifier: Modifier = Modifier, homeViewModel: HomeViewModel) {
    val today = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern(
        "EEEE, d MMMM, yyyy",
        Locale.ENGLISH
    )

    val tasks by homeViewModel.tasks.collectAsState()
    var query by remember { mutableStateOf("") }
    val filteredTasks = tasks.filter {
        it.title.contains(query, ignoreCase = true)
    }

    Column(modifier = modifier.fillMaxSize()) {
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "My tasks",
                fontSize = 32.sp
            )
            Text(
                today.format(formatter),
                fontStyle = FontStyle.Italic,
                color = Color.Gray,
                fontSize = 18.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(4.dp)),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            ViewButton(
                Modifier

                    .padding(6.dp), "All"
            ) {

            }
            ViewButton(
                Modifier

                    .padding(6.dp), "For today"
            ) {

            }
            ViewButton(
                Modifier

                    .padding(6.dp), "Outstanding"
            ) {

            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        SearchBar(
            query = query,
            onQueryChange = { query = it })

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn() {
            items(filteredTasks) { task ->
                TaskItem(task = task, isDone = task.status == TaskStatus.DONE) { checked ->
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
        label = { Text("Search") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    )
}