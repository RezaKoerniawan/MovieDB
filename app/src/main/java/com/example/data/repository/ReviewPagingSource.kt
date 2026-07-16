package com.example.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.core.network.MovieApiService
import com.example.data.mapper.toDomain
import com.example.domain.model.Review

class ReviewPagingSource(
    private val apiService: MovieApiService,
    private val movieId: Int
) : PagingSource<Int, Review>() {

    override fun getRefreshKey(state: PagingState<Int, Review>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Review> {
        val position = params.key ?: 1
        return try {
            val response = apiService.getMovieReviews(movieId = movieId, page = position)
            val reviews = response.results.map { it.toDomain() }

            LoadResult.Page(
                data = reviews,
                prevKey = if (position == 1) null else position - 1,
                nextKey = if (reviews.isEmpty() || position >= response.totalPages) null else position + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }
}
