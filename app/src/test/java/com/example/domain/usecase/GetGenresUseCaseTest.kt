package com.example.domain.usecase

import androidx.paging.PagingData
import com.example.core.network.ResultWrapper
import com.example.domain.model.*
import com.example.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class FakeMovieRepository : MovieRepository {
    var genresResult: Flow<ResultWrapper<List<Genre>>> = flowOf(ResultWrapper.Loading)

    override fun getGenres(): Flow<ResultWrapper<List<Genre>>> = genresResult

    override fun getMoviesByGenre(genreId: String?): Flow<PagingData<Movie>> = flowOf(PagingData.empty())
    override fun getMovieDetails(movieId: Int): Flow<ResultWrapper<MovieDetails>> = flowOf(ResultWrapper.Loading)
    override fun getUserReviews(movieId: Int): Flow<PagingData<Review>> = flowOf(PagingData.empty())
    override fun getMovieTrailers(movieId: Int): Flow<ResultWrapper<List<Video>>> = flowOf(ResultWrapper.Loading)
    override fun getSimilarMovies(movieId: Int): Flow<ResultWrapper<List<Movie>>> = flowOf(ResultWrapper.Loading)
    override fun searchMovies(query: String): Flow<PagingData<Movie>> = flowOf(PagingData.empty())
    override fun getFavoriteMovies(): Flow<List<Movie>> = flowOf(emptyList())
    override suspend fun toggleFavorite(movie: Movie) {}
    override fun isMovieFavorite(movieId: Int): Flow<Boolean> = flowOf(false)
}

class GetGenresUseCaseTest {

    private val repository = FakeMovieRepository()
    private val getGenresUseCase = GetGenresUseCase(repository)

    @Test
    fun `when invoke called, should return flow of genres from repository`() = runTest {
        // Arrange
        val genres = listOf(
            Genre(1, "Aksi"),
            Genre(2, "Drama")
        )
        val expectedResult = ResultWrapper.Success(genres)
        repository.genresResult = flowOf(expectedResult)

        // Act
        val resultList = getGenresUseCase().toList()

        // Assert
        assertEquals(1, resultList.size)
        assertEquals(expectedResult, resultList.first())
    }
}
