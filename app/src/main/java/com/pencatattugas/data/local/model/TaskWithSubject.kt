package com.pencatattugas.data.local.model

import androidx.room.Embedded
import androidx.room.Relation
import com.pencatattugas.data.local.entity.SubjectEntity
import com.pencatattugas.data.local.entity.TaskEntity

data class TaskWithSubject(
    @Embedded
    val task: TaskEntity,

    @Relation(
        parentColumn = "subjectId",
        entityColumn = "id"
    )
    val subject: SubjectEntity?
)
