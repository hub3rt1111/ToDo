package com.example.todo.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState

import androidx.navigation.compose.rememberNavController
import com.example.todo.ui.addtask.AddTask
import com.example.todo.ui.components.TaskItem
import com.example.todo.ui.components.ViewButton
import com.example.todo.ui.settings.Settings
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
@Preview
fun MainScreen(modifier: Modifier = Modifier) {
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
            composable("home") { HomeScreen() }
            composable("addTask") { AddTask() }
            composable("settings") { Settings() }
        }
    }
}

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    val today = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern(
        "EEEE, d MMMM, yyyy",
        Locale.ENGLISH
    )
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
            ViewButton("All") {

            }
            ViewButton("For today") {

            }
            ViewButton("Outstanding ") {

            }
        }
        TaskItem (
            title = "Nauczyć się Compose",
            description = "Card, Row, Column, Theme",
            isDone = false,
            onCheckedChange = { }
        )
    }
}

@Composable
fun BottomBar(navController: NavController, modifier: Modifier = Modifier) {
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
fun SearchBar(modifier: Modifier = Modifier) {
    
}