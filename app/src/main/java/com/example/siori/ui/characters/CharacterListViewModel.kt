package com.example.siori.ui.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.siori.data.Character
import com.example.siori.data.SioriRepository
import com.example.siori.ui.UiState
import com.example.siori.ui.search.QueryNormalizer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CharacterListViewModel(
    private val repository: SioriRepository,
    private val workId: String,
) : ViewModel() {

    private val _state = MutableStateFlow<UiState<List<Character>>>(UiState.Loading)
    val state: StateFlow<UiState<List<Character>>> = _state.asStateFlow()

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    val filtered: StateFlow<List<Character>> = combine(_state, _query) { s, q ->
        val all = (s as? UiState.Success)?.data ?: emptyList()
        if (q.isBlank()) all
        else all.filter { QueryNormalizer.matches(q, it.name, it.aliases) }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    init {
        load()
    }

    fun load() {
        _state.value = UiState.Loading
        viewModelScope.launch {
            _state.value = try {
                UiState.Success(repository.loadCharacters(workId))
            } catch (e: Exception) {
                UiState.Error(e.message ?: "人物情報の読み込みに失敗しました")
            }
        }
    }

    fun onQueryChange(value: String) {
        _query.value = value
    }
}
