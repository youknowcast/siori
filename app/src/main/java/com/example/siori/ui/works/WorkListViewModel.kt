package com.example.siori.ui.works

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.siori.data.SioriRepository
import com.example.siori.data.Work
import com.example.siori.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WorkListViewModel(private val repository: SioriRepository) : ViewModel() {
    private val _state = MutableStateFlow<UiState<List<Work>>>(UiState.Loading)
    val state: StateFlow<UiState<List<Work>>> = _state.asStateFlow()

    init {
        load()
    }

    fun load() {
        _state.value = UiState.Loading
        viewModelScope.launch {
            _state.value = try {
                UiState.Success(repository.loadCatalog())
            } catch (e: Exception) {
                UiState.Error(e.message ?: "作品の読み込みに失敗しました")
            }
        }
    }
}
