package com.example.presentation.genre

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.network.ResultWrapper
import com.example.domain.model.Genre
import com.example.domain.usecase.GetGenresUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class GenreUiState {
    object Loading : GenreUiState()
    data class Success(val genres: List<Genre>) : GenreUiState()
    data class Error(val message: String) : GenreUiState()
}

@HiltViewModel
class GenreViewModel @Inject constructor(
    private val getGenresUseCase: GetGenresUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<GenreUiState>(GenreUiState.Loading)
    val uiState: StateFlow<GenreUiState> = _uiState.asStateFlow()

    init {
        loadGenres()
    }

    fun loadGenres() {
        viewModelScope.launch {
            getGenresUseCase().collect { result ->
                _uiState.value = when (result) {
                    is ResultWrapper.Loading -> GenreUiState.Loading
                    is ResultWrapper.Success -> {
                        if (result.data.isEmpty()) {
                            GenreUiState.Error("Daftar genre kosong.")
                        } else {
                            GenreUiState.Success(result.data)
                        }
                    }
                    is ResultWrapper.Error -> GenreUiState.Error(
                        result.message ?: "Terjadi kesalahan tidak terduga saat memuat genre."
                    )
                }
            }
        }
    }
}
