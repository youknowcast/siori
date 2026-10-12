package com.example.siori.ui.characters

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.siori.data.Character
import com.example.siori.ui.ErrorContent
import com.example.siori.ui.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterListScreen(
    workId: String,
    onOpenRelations: () -> Unit,
    viewModel: CharacterListViewModel,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val query by viewModel.query.collectAsStateWithLifecycle()
    val filtered by viewModel.filtered.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("人物一覧") },
                actions = {
                    IconButton(onClick = onOpenRelations) {
                        Icon(Icons.Filled.AccountTree, contentDescription = "関係図")
                    }
                },
            )
        },
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {
            OutlinedTextField(
                value = query,
                onValueChange = viewModel::onQueryChange,
                singleLine = true,
                label = { Text("名前・別名で検索") },
                modifier = Modifier.fillMaxWidth().padding(16.dp),
            )
            when (val s = state) {
                is UiState.Loading -> CircularProgressIndicator(Modifier.padding(32.dp))
                is UiState.Error -> ErrorContent(
                    message = s.message,
                    onRetry = viewModel::load,
                )
                is UiState.Success -> {
                    if (filtered.isEmpty()) {
                        Text("該当する人物がいません", Modifier.padding(16.dp))
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(filtered, key = { it.id }) { character ->
                                CharacterRow(character)
                                HorizontalDivider()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CharacterRow(character: Character) {
    var expanded by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
            .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        Text(text = character.name, style = MaterialTheme.typography.titleMedium)
        Text(
            text = character.description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        AnimatedVisibility(visible = expanded && character.aliases.isNotEmpty()) {
            Text(
                text = "別名: " + character.aliases.joinToString("、"),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp),
            )
        }
    }
}
