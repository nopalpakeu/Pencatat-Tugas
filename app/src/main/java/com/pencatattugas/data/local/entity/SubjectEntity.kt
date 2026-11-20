package com.pencatattugas.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subjects")
data class SubjectEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val teacherName: String = "",
    val colorHex: Long = 0xFF3F51B5 // Default Indigo
)
