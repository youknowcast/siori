package com.example.siori.ui.relations

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.siori.ui.ErrorContent
import com.example.siori.ui.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RelationsScreen(
    workId: String,
    onBackToCharacters: () -> Unit,
    viewModel: RelationsViewModel,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("関係図") },
                navigationIcon = {
                    IconButton(onClick = onBackToCharacters) {
                        Icon(Icons.AutoMirrored.Filled.List, contentDescription = "人物一覧")
                    }
                },
            )
        },
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            when (val s = state) {
                is UiState.Loading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                is UiState.Error -> ErrorContent(
                    message = s.message,
                    onRetry = viewModel::load,
                    modifier = Modifier.align(Alignment.Center),
                )
                is UiState.Success -> MermaidDiagram(
                    diagram = s.data,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}
