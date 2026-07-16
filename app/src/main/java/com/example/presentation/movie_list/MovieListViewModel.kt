package com.example.presentation.movie_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.domain.model.Movie
import com.example.domain.usecase.GetMoviesByGenreUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest

class MovieListViewModel(
    private val getMoviesByGenreUseCase: GetMoviesByGenreUseCase
) : ViewModel() {

    private val _currentGenreId = MutableStateFlow<String?>(null)
    val currentGenreId = _currentGenreId.asStateFlow()

    val moviesFlow: Flow<PagingData<Movie>> = _currentGenreId.flatMapLatest { genreId ->
        getMoviesByGenreUseCase(genreId)
    }.cachedIn(viewModelScope)

    fun setGenreId(genreId: String?) {
        if (_currentGenreId.value != genreId) {
            _currentGenreId.value = genreId
        }
    }
}
