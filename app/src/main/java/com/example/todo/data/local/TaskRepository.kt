package com.example.todo.data.local

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class TaskRepository(
    private val taskDao: TaskDao
) {
    fun getTasks() = taskDao.getAllTasks()

    suspend fun addTask(task: TaskEntity): Long {
        return taskDao.insert(task)
    }

    suspend fun deleteTask(task: TaskEntity) {
        taskDao.delete(task)
    }

    suspend fun updateTask(task: TaskEntity) {
        taskDao.update(task)
        
        // If task is marked as DONE and has recurrence, create next instance
        if (task.status == TaskStatus.DONE && task.recurrenceType != RecurrenceType.NONE) {
            val nextDueDate = calculateNextDueDate(task)
            if (nextDueDate != null) {
                val newTask = task.copy(
                    id = 0, // New task
                    status = TaskStatus.TODO,
                    dueDate = nextDueDate,
                    createdAt = System.currentTimeMillis()
                )
                taskDao.insert(newTask)
                
                // Set the original task's recurrence to NONE so it doesn't trigger again
                val updatedOriginal = task.copy(recurrenceType = RecurrenceType.NONE)
                taskDao.update(updatedOriginal)
            }
        }
    }

    private fun calculateNextDueDate(task: TaskEntity): Long? {
        val currentDueDate = task.dueDate ?: return null
        val localDateTime = Instant.ofEpochMilli(currentDueDate)
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()
        
        val nextDateTime = when (task.recurrenceType) {
            RecurrenceType.DAILY -> localDateTime.plusDays(1)
            RecurrenceType.WEEKLY -> localDateTime.plusWeeks(1)
            RecurrenceType.MONTHLY -> localDateTime.plusMonths(1)
            RecurrenceType.CUSTOM -> localDateTime.plusDays(task.recurrenceInterval.toLong())
            RecurrenceType.NONE -> return null
        }
        
        return nextDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }

    suspend fun getTaskById(taskId: Long): TaskEntity? {
        return taskDao.getTaskById(taskId)
    }
}
