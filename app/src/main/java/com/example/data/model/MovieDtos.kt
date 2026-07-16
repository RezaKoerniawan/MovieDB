package com.example.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GenreResponseDto(
    @Json(name = "genres") val genres: List<GenreDto>
)

@JsonClass(generateAdapter = true)
data class GenreDto(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String
)

@JsonClass(generateAdapter = true)
data class MovieResponseDto(
    @Json(name = "page") val page: Int,
    @Json(name = "results") val results: List<MovieDto>,
    @Json(name = "total_pages") val totalPages: Int,
    @Json(name = "total_results") val totalResults: Int
)

@JsonClass(generateAdapter = true)
data class MovieDto(
    @Json(name = "id") val id: Int,
    @Json(name = "title") val title: String,
    @Json(name = "original_title") val originalTitle: String?,
    @Json(name = "poster_path") val posterPath: String?,
    @Json(name = "backdrop_path") val backdropPath: String?,
    @Json(name = "release_date") val releaseDate: String?,
    @Json(name = "vote_average") val voteAverage: Double,
    @Json(name = "vote_count") val voteCount: Int,
    @Json(name = "overview") val overview: String,
    @Json(name = "popularity") val popularity: Double,
    @Json(name = "genre_ids") val genreIds: List<Int>?
)

@JsonClass(generateAdapter = true)
data class MovieDetailsResponseDto(
    @Json(name = "id") val id: Int,
    @Json(name = "title") val title: String,
    @Json(name = "original_title") val originalTitle: String?,
    @Json(name = "poster_path") val posterPath: String?,
    @Json(name = "backdrop_path") val backdropPath: String?,
    @Json(name = "release_date") val releaseDate: String?,
    @Json(name = "runtime") val runtime: Int?,
    @Json(name = "vote_average") val voteAverage: Double,
    @Json(name = "vote_count") val voteCount: Int,
    @Json(name = "popularity") val popularity: Double,
    @Json(name = "overview") val overview: String,
    @Json(name = "genres") val genres: List<GenreDto>?,
    @Json(name = "original_language") val originalLanguage: String?,
    @Json(name = "status") val status: String?,
    @Json(name = "budget") val budget: Long?,
    @Json(name = "revenue") val revenue: Long?,
    @Json(name = "homepage") val homepage: String?,
    @Json(name = "production_companies") val productionCompanies: List<ProductionCompanyDto>?
)

@JsonClass(generateAdapter = true)
data class ProductionCompanyDto(
    @Json(name = "id") val id: Int,
    @Json(name = "name") val name: String,
    @Json(name = "logo_path") val logoPath: String?
)

@JsonClass(generateAdapter = true)
data class ReviewResponseDto(
    @Json(name = "id") val id: Int,
    @Json(name = "page") val page: Int,
    @Json(name = "results") val results: List<ReviewDto>,
    @Json(name = "total_pages") val totalPages: Int,
    @Json(name = "total_results") val totalResults: Int
)

@JsonClass(generateAdapter = true)
data class ReviewDto(
    @Json(name = "id") val id: String,
    @Json(name = "author") val author: String,
    @Json(name = "author_details") val authorDetails: AuthorDetailsDto?,
    @Json(name = "content") val content: String,
    @Json(name = "created_at") val createdAt: String
)

@JsonClass(generateAdapter = true)
data class AuthorDetailsDto(
    @Json(name = "username") val username: String,
    @Json(name = "avatar_path") val avatarPath: String?,
    @Json(name = "rating") val rating: Double?
)

@JsonClass(generateAdapter = true)
data class VideoResponseDto(
    @Json(name = "id") val id: Int,
    @Json(name = "results") val results: List<VideoDto>
)

@JsonClass(generateAdapter = true)
data class VideoDto(
    @Json(name = "id") val id: String,
    @Json(name = "key") val key: String,
    @Json(name = "name") val name: String,
    @Json(name = "site") val site: String,
    @Json(name = "type") val type: String
)
