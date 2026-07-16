package com.example.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.core.network.ResultWrapper
import com.example.domain.model.Movie
import com.example.domain.model.MovieDetails
import com.example.domain.model.Review
import com.example.domain.model.Video
import com.example.domain.usecase.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class DetailUiState {
    object Loading : DetailUiState()
    data class Success(val details: MovieDetails) : DetailUiState()
    data class Error(val message: String) : DetailUiState()
}

class DetailViewModel(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val getUserReviewsUseCase: GetUserReviewsUseCase,
    private val getMovieTrailersUseCase: GetMovieTrailersUseCase,
    private val getSimilarMoviesUseCase: GetSimilarMoviesUseCase,
    private val toggleFavoriteMovieUseCase: ToggleFavoriteMovieUseCase,
    private val isMovieFavoriteUseCase: IsMovieFavoriteUseCase
) : ViewModel() {

    private val _movieId = MutableStateFlow<Int?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<DetailUiState> = _movieId.filterNotNull().flatMapLatest { id ->
        getMovieDetailsUseCase(id).map { result ->
            when (result) {
                is ResultWrapper.Loading -> DetailUiState.Loading
                is ResultWrapper.Success -> DetailUiState.Success(result.data)
                is ResultWrapper.Error -> DetailUiState.Error(result.message ?: "Gagal memuat detail film.")
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DetailUiState.Loading)

    @OptIn(ExperimentalCoroutinesApi::class)
    val trailersState: StateFlow<ResultWrapper<List<Video>>> = _movieId.filterNotNull().flatMapLatest { id ->
        getMovieTrailersUseCase(id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ResultWrapper.Loading)

    @OptIn(ExperimentalCoroutinesApi::class)
    val similarMoviesState: StateFlow<ResultWrapper<List<Movie>>> = _movieId.filterNotNull().flatMapLatest { id ->
        getSimilarMoviesUseCase(id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ResultWrapper.Loading)

    @OptIn(ExperimentalCoroutinesApi::class)
    val isFavorite: StateFlow<Boolean> = _movieId.filterNotNull().flatMapLatest { id ->
        isMovieFavoriteUseCase(id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    @OptIn(ExperimentalCoroutinesApi::class)
    val reviewsFlow: Flow<PagingData<Review>> = _movieId.filterNotNull().flatMapLatest { id ->
        getUserReviewsUseCase(id)
    }.cachedIn(viewModelScope)

    fun loadMovieDetails(movieId: Int) {
        _movieId.value = movieId
    }

    fun toggleFavorite(movie: Movie) {
        viewModelScope.launch {
            toggleFavoriteMovieUseCase(movie)
        }
    }
}
