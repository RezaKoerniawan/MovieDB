package com.example.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.core.di.IoDispatcher
import com.example.core.network.MovieApiService
import com.example.core.network.ResultWrapper
import com.example.core.network.toResultWrapper
import com.example.data.local.FavoriteMovieDataStore
import com.example.data.mapper.toDomain
import com.example.data.model.MovieDto
import com.example.domain.model.Genre
import com.example.domain.model.Movie
import com.example.domain.model.MovieDetails
import com.example.domain.model.Review
import com.example.domain.model.Video
import com.example.domain.repository.MovieRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val apiService: MovieApiService,
    private val favoriteDataStore: FavoriteMovieDataStore,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : MovieRepository {

    override fun getGenres(): Flow<ResultWrapper<List<Genre>>> = flow {
        emit(ResultWrapper.Loading)
        try {
            val response = apiService.getGenres()
            val genres = response.genres.map { it.toDomain() }
            emit(ResultWrapper.Success(genres))
        } catch (cancelled: CancellationException) {
            throw cancelled
        } catch (e: Exception) {
            emit(e.toResultWrapper())
        }
    }.flowOn(ioDispatcher)

    override fun getMoviesByGenre(genreId: String?): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                initialLoadSize = 20
            ),
            pagingSourceFactory = { MoviePagingSource(apiService, genreId) }
        ).flow.flowOn(ioDispatcher)
    }

    override fun getMovieDetails(movieId: Int): Flow<ResultWrapper<MovieDetails>> = flow {
        emit(ResultWrapper.Loading)
        try {
            val response = apiService.getMovieDetails(movieId)
            emit(ResultWrapper.Success(response.toDomain()))
        } catch (cancelled: CancellationException) {
            throw cancelled
        } catch (e: Exception) {
            emit(e.toResultWrapper())
        }
    }.flowOn(ioDispatcher)

    override fun getUserReviews(movieId: Int): Flow<PagingData<Review>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { ReviewPagingSource(apiService, movieId) }
        ).flow.flowOn(ioDispatcher)
    }

    override fun getMovieTrailers(movieId: Int): Flow<ResultWrapper<List<Video>>> = flow {
        emit(ResultWrapper.Loading)
        try {
            val response = apiService.getMovieVideos(movieId)
            val videos = response.results
                .filter { it.site.equals("YouTube", ignoreCase = true) }
                .map { it.toDomain() }
                .sortedByDescending { it.isTrailer }
            emit(ResultWrapper.Success(videos))
        } catch (cancelled: CancellationException) {
            throw cancelled
        } catch (e: Exception) {
            emit(e.toResultWrapper())
        }
    }.flowOn(ioDispatcher)

    override fun getSimilarMovies(movieId: Int): Flow<ResultWrapper<List<Movie>>> = flow {
        emit(ResultWrapper.Loading)
        try {
            val response = apiService.getSimilarMovies(movieId)
            val movies = response.results.map { it.toDomain() }
            emit(ResultWrapper.Success(movies))
        } catch (cancelled: CancellationException) {
            throw cancelled
        } catch (e: Exception) {
            emit(e.toResultWrapper())
        }
    }.flowOn(ioDispatcher)

    override fun searchMovies(query: String): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { SearchMoviePagingSource(apiService, query) }
        ).flow.flowOn(ioDispatcher)
    }

    override fun getFavoriteMovies(): Flow<List<Movie>> {
        return favoriteDataStore.favoriteMoviesFlow.map { list ->
            list.map { it.toDomain() }
        }.flowOn(ioDispatcher)
    }

    override suspend fun toggleFavorite(movie: Movie) {
        favoriteDataStore.toggleFavorite(movie.toDto())
    }

    override fun isMovieFavorite(movieId: Int): Flow<Boolean> {
        return favoriteDataStore.favoriteMoviesFlow.map { list ->
            list.any { it.id == movieId }
        }.flowOn(ioDispatcher)
    }

    private fun Movie.toDto(): MovieDto {
        val partialPosterPath = posterUrl?.substringAfter("https://image.tmdb.org/t/p/w500")
        val partialBackdropPath = backdropUrl?.substringAfter("https://image.tmdb.org/t/p/w780")
        return MovieDto(
            id = id,
            title = title,
            originalTitle = originalTitle,
            posterPath = partialPosterPath,
            backdropPath = partialBackdropPath,
            releaseDate = releaseDate,
            voteAverage = voteAverage,
            voteCount = voteCount,
            overview = overview,
            popularity = popularity,
            genreIds = emptyList()
        )
    }
}
