package com.example.todo.data.local

import kotlinx.coroutines.flow.Flow

class LocationRepository(
    private val locationDao: LocationDao
) {
    fun getLocationsForTask(taskId: Long): Flow<List<LocationEntity>> =
        locationDao.getLocationsForTask(taskId)

    suspend fun insert(location: LocationEntity) {
        locationDao.insert(location)
    }

    suspend fun update(location: LocationEntity) {
        locationDao.update(location)
    }

    suspend fun delete(location: LocationEntity) {
        locationDao.delete(location)
    }
}
