package com.example.todo.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AttachmentDao {
    @Insert
    suspend fun insert(attachment: AttachmentEntity): Long

    @Update
    suspend fun update(attachment: AttachmentEntity)

    @Delete
    suspend fun delete(attachment: AttachmentEntity)

    @Query("SELECT * FROM AttachmentEntity WHERE taskId = :taskId")
    fun getAttachmentsForTask(taskId: Long): Flow<List<AttachmentEntity>>

    @Query("SELECT COUNT(*) FROM AttachmentEntity WHERE taskId = :taskId")
    fun getAttachmentsCountForTask(taskId: Long): Flow<Int>
}