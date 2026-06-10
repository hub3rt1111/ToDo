package com.example.todo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.todo.ui.ToDoViewModelFactory
import com.example.todo.ui.home.MainScreen
import com.example.todo.ui.theme.ToDoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val taskRepository = (application as TodoApplication).taskRepository
        val attachmentRepository = (application as TodoApplication).attachmentRepository
        val factory = ToDoViewModelFactory(taskRepository, attachmentRepository)

        enableEdgeToEdge()
        setContent {
            ToDoTheme {
                MainScreen(factory)
            }
        }
    }
}