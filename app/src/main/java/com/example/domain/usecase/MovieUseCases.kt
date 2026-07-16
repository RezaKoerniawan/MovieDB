package com.example.domain.usecase

import androidx.paging.PagingData
import com.example.core.network.ResultWrapper
import com.example.domain.model.*
import com.example.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class GetGenresUseCase(private val repository: MovieRepository) {
    operator fun invoke(): Flow<ResultWrapper<List<Genre>>> {
        return repository.getGenres()
    }
}

class GetMoviesByGenreUseCase(private val repository: MovieRepository) {
    operator fun invoke(genreId: String?): Flow<PagingData<Movie>> {
        return repository.getMoviesByGenre(genreId)
    }
}

class GetMovieDetailsUseCase(private val repository: MovieRepository) {
    operator fun invoke(movieId: Int): Flow<ResultWrapper<MovieDetails>> {
        return repository.getMovieDetails(movieId)
    }
}

class GetUserReviewsUseCase(private val repository: MovieRepository) {
    operator fun invoke(movieId: Int): Flow<PagingData<Review>> {
        return repository.getUserReviews(movieId)
    }
}

class GetMovieTrailersUseCase(private val repository: MovieRepository) {
    operator fun invoke(movieId: Int): Flow<ResultWrapper<List<Video>>> {
        return repository.getMovieTrailers(movieId)
    }
}

class GetSimilarMoviesUseCase(private val repository: MovieRepository) {
    operator fun invoke(movieId: Int): Flow<ResultWrapper<List<Movie>>> {
        return repository.getSimilarMovies(movieId)
    }
}

class SearchMoviesUseCase(private val repository: MovieRepository) {
    operator fun invoke(query: String): Flow<PagingData<Movie>> {
        return repository.searchMovies(query)
    }
}

class GetFavoriteMoviesUseCase(private val repository: MovieRepository) {
    operator fun invoke(): Flow<List<Movie>> {
        return repository.getFavoriteMovies()
    }
}

class ToggleFavoriteMovieUseCase(private val repository: MovieRepository) {
    suspend operator fun invoke(movie: Movie) {
        repository.toggleFavorite(movie)
    }
}

class IsMovieFavoriteUseCase(private val repository: MovieRepository) {
    operator fun invoke(movieId: Int): Flow<Boolean> {
        return repository.isMovieFavorite(movieId)
    }
}
