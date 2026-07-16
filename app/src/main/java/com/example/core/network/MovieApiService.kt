package com.example.core.network

import com.example.data.model.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiService {

    @GET("genre/movie/list")
    suspend fun getGenres(
        @Query("language") language: String = "id-ID"
    ): GenreResponseDto

    @GET("discover/movie")
    suspend fun discoverMovies(
        @Query("with_genres") genreId: String?,
        @Query("page") page: Int,
        @Query("language") language: String = "id-ID",
        @Query("sort_by") sortBy: String = "popularity.desc"
    ): MovieResponseDto

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String = "id-ID"
    ): MovieDetailsResponseDto

    @GET("movie/{movie_id}/reviews")
    suspend fun getMovieReviews(
        @Path("movie_id") movieId: Int,
        @Query("page") page: Int,
        @Query("language") language: String = "en-US"
    ): ReviewResponseDto

    @GET("movie/{movie_id}/videos")
    suspend fun getMovieVideos(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String = "en-US"
    ): VideoResponseDto

    @GET("movie/{movie_id}/similar")
    suspend fun getSimilarMovies(
        @Path("movie_id") movieId: Int,
        @Query("language") language: String = "id-ID"
    ): MovieResponseDto

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("language") language: String = "id-ID"
    ): MovieResponseDto
}
