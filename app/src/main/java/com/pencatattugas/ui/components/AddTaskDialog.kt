package com.pencatattugas.ui.components

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pencatattugas.data.local.entity.SubjectEntity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AddTaskDialog(
    subjects: List<SubjectEntity>,
    onDismiss: () -> Unit,
    onSaveTask: (
        title: String,
        description: String,
        subjectId: Long,
        taskType: String,
        priority: Int,
        deadlineTimestamp: Long
    ) -> Unit
) {
    val context = LocalContext.current
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedSubjectId by remember { mutableStateOf(subjects.firstOrNull()?.id ?: 0L) }
    var selectedType by remember { mutableStateOf("PR") }
    var selectedPriority by remember { mutableIntStateOf(2) } // 2: Sedang

    // Default deadline: Besok jam 23:59
    val defaultDeadline = remember {
        Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, 1)
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 0)
        }.timeInMillis
    }
    var deadlineTimestamp by remember { mutableLongStateOf(defaultDeadline) }

    val taskTypes = listOf("PR", "Proyek", "Kuis", "Makalah", "Latihan")

    val dateFormat = SimpleDateFormat("EEE, d MMM yyyy - HH:mm", Locale("id", "ID"))

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Catat Tugas Baru",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                // Judul Tugas
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Nama Tugas / PR *") },
                    placeholder = { Text("Contoh: Kerjakan Hal 45 No 1-10") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Deskripsi Tambahan
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Catatan / Instruksi Guru") },
                    placeholder = { Text("Contoh: Tulis di buku berpetak, kumpul di meja guru") },
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Pilih Mata Pelajaran
                Text(
                    text = "Mata Pelajaran",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    subjects.forEach { subject ->
                        val isSelected = selectedSubjectId == subject.id
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedSubjectId = subject.id },
                            label = { Text(subject.name) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(subject.colorHex).copy(alpha = 0.2f),
                                selectedLabelColor = Color(subject.colorHex)
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Tipe Tugas
                Text(
                    text = "Tipe Kegiatan Akademis",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    taskTypes.forEach { type ->
                        FilterChip(
                            selected = selectedType == type,
                            onClick = { selectedType = type },
                            label = { Text(type) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Prioritas
                Text(
                    text = "Prioritas",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val priorities = listOf(1 to "Rendah", 2 to "Sedang", 3 to "Tinggi")
                    priorities.forEach { (level, label) ->
                        FilterChip(
                            selected = selectedPriority == level,
                            onClick = { selectedPriority = level },
                            label = { Text(label) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Tenggat Waktu (Deadline)
                Text(
                    text = "Tenggat Waktu (Deadline)",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))

                // Quick buttons for students
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    SuggestionChip(
                        onClick = {
                            val cal = Calendar.getInstance().apply {
                                set(Calendar.HOUR_OF_DAY, 23)
                                set(Calendar.MINUTE, 59)
                            }
                            deadlineTimestamp = cal.timeInMillis
                        },
                        label = { Text("Hari Ini", fontSize = 12.sp) }
                    )
                    SuggestionChip(
                        onClick = {
                            val cal = Calendar.getInstance().apply {
                                add(Calendar.DAY_OF_YEAR, 1)
                                set(Calendar.HOUR_OF_DAY, 23)
                                set(Calendar.MINUTE, 59)
                            }
                            deadlineTimestamp = cal.timeInMillis
                        },
                        label = { Text("Besok", fontSize = 12.sp) }
                    )
                    SuggestionChip(
                        onClick = {
                            val cal = Calendar.getInstance().apply {
                                add(Calendar.DAY_OF_YEAR, 3)
                                set(Calendar.HOUR_OF_DAY, 23)
                                set(Calendar.MINUTE, 59)
                            }
                            deadlineTimestamp = cal.timeInMillis
                        },
                        label = { Text("+3 Hari", fontSize = 12.sp) }
                    )
                    SuggestionChip(
                        onClick = {
                            val cal = Calendar.getInstance().apply {
                                add(Calendar.DAY_OF_YEAR, 7)
                                set(Calendar.HOUR_OF_DAY, 23)
                                set(Calendar.MINUTE, 59)
                            }
                            deadlineTimestamp = cal.timeInMillis
                        },
                        label = { Text("+1 Minggu", fontSize = 12.sp) }
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Custom Date & Time Picker trigger
                OutlinedButton(
                    onClick = {
                        val currentCal = Calendar.getInstance().apply { timeInMillis = deadlineTimestamp }
                        DatePickerDialog(
                            context,
                            { _, year, month, dayOfMonth ->
                                TimePickerDialog(
                                    context,
                                    { _, hourOfDay, minute ->
                                        val newCal = Calendar.getInstance().apply {
                                            set(year, month, dayOfMonth, hourOfDay, minute, 0)
                                        }
                                        deadlineTimestamp = newCal.timeInMillis
                                    },
                                    currentCal.get(Calendar.HOUR_OF_DAY),
                                    currentCal.get(Calendar.MINUTE),
                                    true
                                ).show()
                            },
                            currentCal.get(Calendar.YEAR),
                            currentCal.get(Calendar.MONTH),
                            currentCal.get(Calendar.DAY_OF_MONTH)
                        ).show()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(text = dateFormat.format(Date(deadlineTimestamp)))
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onSaveTask(
                            title,
                            description,
                            selectedSubjectId,
                            selectedType,
                            selectedPriority,
                            deadlineTimestamp
                        )
                        onDismiss()
                    }
                },
                enabled = title.isNotBlank() && selectedSubjectId != 0L
            ) {
                Text("Simpan Tugas")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}
