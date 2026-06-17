package com.example.todo.data.local


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
    }

    suspend fun getTaskById(taskId: Long): TaskEntity? {
        return taskDao.getTaskById(taskId)
    }
}