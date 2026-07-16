package com.example.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.domain.model.Movie
import com.example.domain.usecase.SearchMoviesUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

class SearchViewModel(
    private val searchMoviesUseCase: SearchMoviesUseCase
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val searchResult: Flow<PagingData<Movie>> = _query
        .debounce(500)
        .filter { it.isNotBlank() }
        .flatMapLatest { query ->
            searchMoviesUseCase(query)
        }
        .cachedIn(viewModelScope)

    fun searchMovies(query: String) {
        _query.value = query
    }
}
