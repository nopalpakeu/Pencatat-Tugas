package com.pencatattugas.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.pencatattugas.data.local.entity.TaskEntity
import com.pencatattugas.data.local.model.TaskWithSubject
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Transaction
    @Query("SELECT * FROM tasks ORDER BY isCompleted ASC, deadlineTimestamp ASC")
    fun getAllTasksWithSubject(): Flow<List<TaskWithSubject>>

    @Transaction
    @Query("SELECT * FROM tasks WHERE isCompleted = 0 ORDER BY deadlineTimestamp ASC")
    fun getActiveTasks(): Flow<List<TaskWithSubject>>

    @Transaction
    @Query("SELECT * FROM tasks WHERE isCompleted = 1 ORDER BY completedAt DESC, deadlineTimestamp DESC")
    fun getCompletedTasks(): Flow<List<TaskWithSubject>>

    @Transaction
    @Query("SELECT * FROM tasks WHERE subjectId = :subjectId ORDER BY isCompleted ASC, deadlineTimestamp ASC")
    fun getTasksBySubject(subjectId: Long): Flow<List<TaskWithSubject>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity): Long

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Query("UPDATE tasks SET isCompleted = :isCompleted, completedAt = :completedAt WHERE id = :taskId")
    suspend fun setTaskCompletion(taskId: Long, isCompleted: Boolean, completedAt: Long?)

    @Delete
    suspend fun deleteTask(task: TaskEntity)

    @Query("DELETE FROM tasks WHERE id = :taskId")
    suspend fun deleteTaskById(taskId: Long)
}
