package com.example.domain.usecase

import androidx.paging.PagingData
import com.example.core.network.ResultWrapper
import com.example.domain.model.*
import com.example.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGenresUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    operator fun invoke(): Flow<ResultWrapper<List<Genre>>> =
        repository.getGenres()
}

class GetMoviesByGenreUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    operator fun invoke(genreId: String?): Flow<PagingData<Movie>> =
        repository.getMoviesByGenre(genreId)
}

class GetMovieDetailsUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    operator fun invoke(movieId: Int): Flow<ResultWrapper<MovieDetails>> =
        repository.getMovieDetails(movieId)
}

class GetUserReviewsUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    operator fun invoke(movieId: Int): Flow<PagingData<Review>> =
        repository.getUserReviews(movieId)
}

class GetMovieTrailersUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    operator fun invoke(movieId: Int): Flow<ResultWrapper<List<Video>>> =
        repository.getMovieTrailers(movieId)
}

class GetSimilarMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    operator fun invoke(movieId: Int): Flow<ResultWrapper<List<Movie>>> =
        repository.getSimilarMovies(movieId)
}

class SearchMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    operator fun invoke(query: String): Flow<PagingData<Movie>> =
        repository.searchMovies(query)
}

class GetFavoriteMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    operator fun invoke(): Flow<List<Movie>> =
        repository.getFavoriteMovies()
}

class ToggleFavoriteMovieUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(movie: Movie) {
        repository.toggleFavorite(movie)
    }
}

class IsMovieFavoriteUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    operator fun invoke(movieId: Int): Flow<Boolean> =
        repository.isMovieFavorite(movieId)
}
