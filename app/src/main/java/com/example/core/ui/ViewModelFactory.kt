package com.example.core.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.MovieApplication
import com.example.core.di.AppContainer
import com.example.presentation.genre.GenreViewModel
import com.example.presentation.movie_list.MovieListViewModel
import com.example.presentation.detail.DetailViewModel
import com.example.presentation.favorite.FavoriteViewModel
import com.example.presentation.search.SearchViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val appContainer: AppContainer
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(GenreViewModel::class.java) -> {
                GenreViewModel(appContainer.getGenresUseCase) as T
            }
            modelClass.isAssignableFrom(MovieListViewModel::class.java) -> {
                MovieListViewModel(appContainer.getMoviesByGenreUseCase) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(
                    getMovieDetailsUseCase = appContainer.getMovieDetailsUseCase,
                    getUserReviewsUseCase = appContainer.getUserReviewsUseCase,
                    getMovieTrailersUseCase = appContainer.getMovieTrailersUseCase,
                    getSimilarMoviesUseCase = appContainer.getSimilarMoviesUseCase,
                    toggleFavoriteMovieUseCase = appContainer.toggleFavoriteMovieUseCase,
                    isMovieFavoriteUseCase = appContainer.isMovieFavoriteUseCase
                ) as T
            }
            modelClass.isAssignableFrom(FavoriteViewModel::class.java) -> {
                FavoriteViewModel(appContainer.getFavoriteMoviesUseCase) as T
            }
            modelClass.isAssignableFrom(SearchViewModel::class.java) -> {
                SearchViewModel(appContainer.searchMoviesUseCase) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}


fun CreationExtras.movieApplication(): MovieApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MovieApplication)
