package com.example.siori.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.siori.data.SioriRepository
import com.example.siori.ui.works.WorkListScreen
import com.example.siori.ui.works.WorkListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SioriApp(repository: SioriRepository) {
    val navController = rememberNavController()
    Surface(color = MaterialTheme.colorScheme.background) {
        NavHost(navController = navController, startDestination = "works") {
            composable("works") {
                Scaffold(topBar = { TopAppBar(title = { Text("しおり") }) }) { padding ->
                    WorkListScreen(
                        onWorkClick = { workId ->
                            navController.navigate("works/$workId/characters")
                        },
                        viewModel = viewModel { WorkListViewModel(repository) },
                    )
                }
            }
        }
    }
}
