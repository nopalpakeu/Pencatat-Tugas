package com.pencatattugas.data.repository

import com.pencatattugas.data.local.dao.SubjectDao
import com.pencatattugas.data.local.dao.TaskDao
import com.pencatattugas.data.local.entity.SubjectEntity
import com.pencatattugas.data.local.entity.TaskEntity
import com.pencatattugas.data.local.model.TaskWithSubject
import kotlinx.coroutines.flow.Flow

class AcademicRepository(
    private val subjectDao: SubjectDao,
    private val taskDao: TaskDao
) {
    val allSubjects: Flow<List<SubjectEntity>> = subjectDao.getAllSubjects()
    val activeTasks: Flow<List<TaskWithSubject>> = taskDao.getActiveTasks()
    val completedTasks: Flow<List<TaskWithSubject>> = taskDao.getCompletedTasks()
    val allTasks: Flow<List<TaskWithSubject>> = taskDao.getAllTasksWithSubject()

    fun getTasksBySubject(subjectId: Long): Flow<List<TaskWithSubject>> {
        return taskDao.getTasksBySubject(subjectId)
    }

    suspend fun insertSubject(subject: SubjectEntity): Long = subjectDao.insertSubject(subject)

    suspend fun updateSubject(subject: SubjectEntity) = subjectDao.updateSubject(subject)

    suspend fun deleteSubject(subject: SubjectEntity) = subjectDao.deleteSubject(subject)

    suspend fun insertTask(task: TaskEntity): Long = taskDao.insertTask(task)

    suspend fun updateTask(task: TaskEntity) = taskDao.updateTask(task)

    suspend fun deleteTask(task: TaskEntity) = taskDao.deleteTask(task)

    suspend fun deleteTaskById(taskId: Long) = taskDao.deleteTaskById(taskId)

    suspend fun toggleTaskCompletion(taskId: Long, isCompleted: Boolean) {
        val completedAt = if (isCompleted) System.currentTimeMillis() else null
        taskDao.setTaskCompletion(taskId, isCompleted, completedAt)
    }
}
