package com.example.domain.repository

import androidx.paging.PagingData
import com.example.core.network.ResultWrapper
import com.example.domain.model.*
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    fun getGenres(): Flow<ResultWrapper<List<Genre>>>
    fun getMoviesByGenre(genreId: String?): Flow<PagingData<Movie>>
    fun getMovieDetails(movieId: Int): Flow<ResultWrapper<MovieDetails>>
    fun getUserReviews(movieId: Int): Flow<PagingData<Review>>
    fun getMovieTrailers(movieId: Int): Flow<ResultWrapper<List<Video>>>
    fun getSimilarMovies(movieId: Int): Flow<ResultWrapper<List<Movie>>>
    fun searchMovies(query: String): Flow<PagingData<Movie>>
    fun getFavoriteMovies(): Flow<List<Movie>>
    suspend fun toggleFavorite(movie: Movie)
    fun isMovieFavorite(movieId: Int): Flow<Boolean>
}
