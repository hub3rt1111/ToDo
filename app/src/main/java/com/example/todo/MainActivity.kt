package com.example.todo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.todo.ui.ToDoViewModelFactory
import com.example.todo.ui.home.MainScreen
import com.example.todo.ui.theme.ToDoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val app = (application as TodoApplication)
        val taskRepository = app.taskRepository
        val attachmentRepository = app.attachmentRepository
        val settingsRepository = app.settingsRepository
        val factory = ToDoViewModelFactory(taskRepository, attachmentRepository, settingsRepository)




        enableEdgeToEdge()
        setContent {

            val themeMode by settingsRepository.themeMode.collectAsState(initial = "SYSTEM")
            val darkTheme = when (themeMode) {
                "DARK" -> true
                "LIGHT" -> false
                else -> androidx.compose.foundation.isSystemInDarkTheme()
            }
            ToDoTheme(darkTheme = darkTheme) {
                MainScreen(factory)
            }

        }
    }
}