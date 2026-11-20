package com.pencatattugas.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pencatattugas.ui.components.TaskCard
import com.pencatattugas.ui.viewmodel.AcademicViewModel
import com.pencatattugas.ui.viewmodel.TaskFilter

@Composable
fun HomeScreen(
    viewModel: AcademicViewModel,
    modifier: Modifier = Modifier
) {
    val tasks by viewModel.filteredActiveTasks.collectAsState()
    val subjects by viewModel.allSubjects.collectAsState()
    val currentFilter by viewModel.currentFilter.collectAsState()
    val selectedSubjectId by viewModel.selectedSubjectIdFilter.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // Ringkasan Header
        Text(
            text = "Agenda Belajar",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = "${tasks.size} tugas aktif perlu diselesaikan",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Filter Bar (Semua, Hari Ini, Mendatang, Terlambat)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TaskFilter.values().forEach { filter ->
                FilterChip(
                    selected = currentFilter == filter,
                    onClick = { viewModel.setFilter(filter) },
                    label = { Text(filter.label) }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Filter per Mata Pelajaran (Chips)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // "Semua Mapel"
            FilterChip(
                selected = selectedSubjectId == null,
                onClick = { viewModel.setSubjectFilter(null) },
                label = { Text("Semua Mapel", fontSize = 12.sp) }
            )

            subjects.forEach { subject ->
                val isSelected = selectedSubjectId == subject.id
                val subjectColor = Color(subject.colorHex)
                FilterChip(
                    selected = isSelected,
                    onClick = {
                        viewModel.setSubjectFilter(if (isSelected) null else subject.id)
                    },
                    label = { Text(subject.name, fontSize = 12.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = subjectColor.copy(alpha = 0.2f),
                        selectedLabelColor = subjectColor
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Daftar Tugas
        if (tasks.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.AssignmentTurnedIn,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Tidak ada tugas di kategori ini!",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Semua tugas selesai atau belum ada tugas baru",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(tasks, key = { it.task.id }) { taskWithSubject ->
                    TaskCard(
                        taskWithSubject = taskWithSubject,
                        onToggleCompletion = { isCompleted ->
                            viewModel.toggleTask(taskWithSubject.task.id, isCompleted)
                        },
                        onDelete = {
                            viewModel.deleteTask(taskWithSubject.task)
                        }
                    )
                }
            }
        }
    }
}
