package com.pencatattugas

import android.app.Application
import com.pencatattugas.data.local.AppDatabase
import com.pencatattugas.data.repository.AcademicRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MainApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy { AppDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { AcademicRepository(database.subjectDao(), database.taskDao()) }
}
