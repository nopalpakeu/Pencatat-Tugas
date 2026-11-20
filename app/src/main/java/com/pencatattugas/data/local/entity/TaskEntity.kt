package com.pencatattugas.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tasks",
    foreignKeys = [
        ForeignKey(
            entity = SubjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["subjectId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["subjectId"])]
)
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val subjectId: Long,
    val title: String,
    val description: String = "",
    val taskType: String = "PR", // PR, Proyek, Kuis, Makalah, Latihan
    val priority: Int = 2,       // 1: Rendah, 2: Sedang, 3: Tinggi
    val deadlineTimestamp: Long, // Epoch millis
    val isCompleted: Boolean = false,
    val completedAt: Long? = null
)
