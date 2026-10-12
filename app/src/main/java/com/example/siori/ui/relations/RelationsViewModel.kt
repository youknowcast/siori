package com.example.siori.ui.relations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.siori.data.SioriRepository
import com.example.siori.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RelationsViewModel(
    private val repository: SioriRepository,
    private val workId: String,
) : ViewModel() {

    private val _state = MutableStateFlow<UiState<String>>(UiState.Loading)
    val state: StateFlow<UiState<String>> = _state.asStateFlow()

    init {
        load()
    }

    fun load() {
        _state.value = UiState.Loading
        viewModelScope.launch {
            _state.value = try {
                UiState.Success(repository.loadRelations(workId))
            } catch (e: Exception) {
                UiState.Error(e.message ?: "関係図の読み込みに失敗しました")
            }
        }
    }
}
