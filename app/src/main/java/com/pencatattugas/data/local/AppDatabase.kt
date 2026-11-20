package com.pencatattugas.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.pencatattugas.data.local.dao.SubjectDao
import com.pencatattugas.data.local.dao.TaskDao
import com.pencatattugas.data.local.entity.SubjectEntity
import com.pencatattugas.data.local.entity.TaskEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [SubjectEntity::class, TaskEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun subjectDao(): SubjectDao
    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "pencatat_tugas.db"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(AppDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class AppDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateInitialSubjects(database.subjectDao())
                }
            }
        }

        private suspend fun populateInitialSubjects(subjectDao: SubjectDao) {
            val initialSubjects = listOf(
                SubjectEntity(name = "Matematika", teacherName = "Guru Matematika", colorHex = 0xFF1E88E5), // Blue
                SubjectEntity(name = "Fisika", teacherName = "Guru Fisika", colorHex = 0xFF8E24AA),      // Purple
                SubjectEntity(name = "Bahasa Indonesia", teacherName = "Guru B. Indo", colorHex = 0xFF43A047), // Green
                SubjectEntity(name = "Bahasa Inggris", teacherName = "Guru B. Inggris", colorHex = 0xFFFB8C00), // Orange
                SubjectEntity(name = "Biologi", teacherName = "Guru Biologi", colorHex = 0xFF00ACC1),   // Cyan
                SubjectEntity(name = "Kimia", teacherName = "Guru Kimia", colorHex = 0xFFE53935)        // Red
            )
            subjectDao.insertAll(initialSubjects)
        }
    }
}
