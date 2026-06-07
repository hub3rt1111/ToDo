package com.example.todo.data.local


class TaskRepository(
    private val taskDao: TaskDao
) {
    fun getTasks() = taskDao.getAllTasks()

    suspend fun addTask(task: TaskEntity) {
        taskDao.insert(task)
    }

    suspend fun deleteTask(task: TaskEntity) {
        taskDao.delete(task)
    }
}