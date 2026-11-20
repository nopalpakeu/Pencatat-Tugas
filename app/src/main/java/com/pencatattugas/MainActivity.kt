package com.pencatattugas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.pencatattugas.ui.components.AddSubjectDialog
import com.pencatattugas.ui.components.AddTaskDialog
import com.pencatattugas.ui.screens.HistoryScreen
import com.pencatattugas.ui.screens.HomeScreen
import com.pencatattugas.ui.screens.SubjectListScreen
import com.pencatattugas.ui.theme.PencatatTugasTheme
import com.pencatattugas.ui.viewmodel.AcademicViewModel
import com.pencatattugas.ui.viewmodel.AcademicViewModelFactory

class MainActivity : ComponentActivity() {

    private val viewModel: AcademicViewModel by viewModels {
        val app = application as MainApplication
        AcademicViewModelFactory(app.repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PencatatTugasTheme {
                MainAppScreen(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppScreen(viewModel: AcademicViewModel) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var showAddTaskDialog by remember { mutableStateOf(false) }
    var showAddSubjectDialog by remember { mutableStateOf(false) }

    val subjects by viewModel.allSubjects.collectAsState()

    val navItems = listOf(
        Triple("Tugas", Icons.Default.Assignment, 0),
        Triple("Mapel", Icons.Default.School, 1),
        Triple("Riwayat", Icons.Default.History, 2)
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                navItems.forEach { (label, icon, index) ->
                    NavigationBarItem(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        icon = { Icon(imageVector = icon, contentDescription = label) },
                        label = { Text(label) }
                    )
                }
            }
        },
        floatingActionButton = {
            // Tampilkan FAB hanya di halaman Tugas (tab 0)
            if (selectedTab == 0) {
                ExtendedFloatingActionButton(
                    onClick = { showAddTaskDialog = true },
                    icon = { Icon(imageVector = Icons.Default.Add, contentDescription = "Tambah Tugas") },
                    text = { Text("Tugas Baru") },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    ) { innerPadding ->
        val screenModifier = Modifier.padding(innerPadding)

        when (selectedTab) {
            0 -> HomeScreen(viewModel = viewModel, modifier = screenModifier)
            1 -> SubjectListScreen(
                viewModel = viewModel,
                onOpenAddSubjectDialog = { showAddSubjectDialog = true },
                modifier = screenModifier
            )
            2 -> HistoryScreen(viewModel = viewModel, modifier = screenModifier)
        }

        // Dialog Tambah Tugas Baru
        if (showAddTaskDialog) {
            AddTaskDialog(
                subjects = subjects,
                onDismiss = { showAddTaskDialog = false },
                onSaveTask = { title, description, subjectId, taskType, priority, deadline ->
                    viewModel.addTask(title, description, subjectId, taskType, priority, deadline)
                }
            )
        }

        // Dialog Tambah Mata Pelajaran Baru
        if (showAddSubjectDialog) {
            AddSubjectDialog(
                onDismiss = { showAddSubjectDialog = false },
                onSaveSubject = { name, teacher, colorHex ->
                    viewModel.addSubject(name, teacher, colorHex)
                }
            )
        }
    }
}
