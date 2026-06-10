package com.example.todo.data.local

class AttachmentRepository(
    private val dao: AttachmentDao
) {

    suspend fun insert(attachment: AttachmentEntity): Long {
        return dao.insert(attachment)
    }

    suspend fun update(attachment: AttachmentEntity) {
        dao.update(attachment)
    }

    suspend fun delete(attachment: AttachmentEntity) {
        dao.delete(attachment)
    }

    fun getAttachmentsForTask(taskId: Long) =
        dao.getAttachmentsForTask(taskId)
}