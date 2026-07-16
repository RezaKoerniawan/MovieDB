package com.example.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.core.network.MovieApiService
import com.example.data.mapper.toDomain
import com.example.domain.model.Movie


class MoviePagingSource(
    private val apiService: MovieApiService,
    private val genreId: String?
) : PagingSource<Int, Movie>() {

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val position = params.key ?: 1
        return try {
            val response = apiService.discoverMovies(genreId = genreId, page = position)
            val movies = response.results.map { it.toDomain() }

            LoadResult.Page(
                data = movies,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (movies.isEmpty() || position >= response.totalPages) null else position + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }
}
