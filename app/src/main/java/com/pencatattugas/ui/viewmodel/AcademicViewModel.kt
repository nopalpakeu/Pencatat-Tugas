package com.pencatattugas.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pencatattugas.data.local.entity.SubjectEntity
import com.pencatattugas.data.local.entity.TaskEntity
import com.pencatattugas.data.local.model.TaskWithSubject
import com.pencatattugas.data.repository.AcademicRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

class AcademicViewModel(
    private val repository: AcademicRepository
) : ViewModel() {

    val allSubjects: StateFlow<List<SubjectEntity>> = repository.allSubjects
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val completedTasks: StateFlow<List<TaskWithSubject>> = repository.completedTasks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _currentFilter = MutableStateFlow(TaskFilter.ALL)
    val currentFilter: StateFlow<TaskFilter> = _currentFilter.asStateFlow()

    private val _selectedSubjectIdFilter = MutableStateFlow<Long?>(null)
    val selectedSubjectIdFilter: StateFlow<Long?> = _selectedSubjectIdFilter.asStateFlow()

    val filteredActiveTasks: StateFlow<List<TaskWithSubject>> = combine(
        repository.activeTasks,
        _currentFilter,
        _selectedSubjectIdFilter
    ) { tasks, filter, subjectId ->
        val now = System.currentTimeMillis()
        val startOfToday = getStartOfDay(now)
        val endOfToday = getEndOfDay(now)

        tasks.filter { item ->
            // Filter per subject jika dipilih
            if (subjectId != null && item.task.subjectId != subjectId) {
                return@filter false
            }

            // Filter status/deadline
            when (filter) {
                TaskFilter.ALL -> true
                TaskFilter.TODAY -> item.task.deadlineTimestamp in startOfToday..endOfToday
                TaskFilter.UPCOMING -> item.task.deadlineTimestamp > endOfToday
                TaskFilter.OVERDUE -> item.task.deadlineTimestamp < now
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setFilter(filter: TaskFilter) {
        _currentFilter.value = filter
    }

    fun setSubjectFilter(subjectId: Long?) {
        _selectedSubjectIdFilter.value = subjectId
    }

    fun addTask(
        title: String,
        description: String,
        subjectId: Long,
        taskType: String,
        priority: Int,
        deadlineTimestamp: Long
    ) {
        viewModelScope.launch {
            val task = TaskEntity(
                title = title.trim(),
                description = description.trim(),
                subjectId = subjectId,
                taskType = taskType,
                priority = priority,
                deadlineTimestamp = deadlineTimestamp,
                isCompleted = false
            )
            repository.insertTask(task)
        }
    }

    fun toggleTask(taskId: Long, isCompleted: Boolean) {
        viewModelScope.launch {
            repository.toggleTaskCompletion(taskId, isCompleted)
        }
    }

    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }

    fun addSubject(name: String, teacherName: String, colorHex: Long) {
        viewModelScope.launch {
            val subject = SubjectEntity(
                name = name.trim(),
                teacherName = teacherName.trim(),
                colorHex = colorHex
            )
            repository.insertSubject(subject)
        }
    }

    fun deleteSubject(subject: SubjectEntity) {
        viewModelScope.launch {
            repository.deleteSubject(subject)
        }
    }

    private fun getStartOfDay(timeMillis: Long): Long {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = timeMillis
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return calendar.timeInMillis
    }

    private fun getEndOfDay(timeMillis: Long): Long {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = timeMillis
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }
        return calendar.timeInMillis
    }
}
