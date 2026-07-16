package com.example.domain.model

data class Genre(
    val id: Int,
    val name: String
)

data class Movie(
    val id: Int,
    val title: String,
    val originalTitle: String,
    val posterUrl: String?,
    val backdropUrl: String?,
    val releaseDate: String,
    val voteAverage: Double,
    val voteCount: Int,
    val overview: String,
    val popularity: Double
)

data class MovieDetails(
    val id: Int,
    val title: String,
    val originalTitle: String,
    val posterUrl: String?,
    val backdropUrl: String?,
    val releaseDate: String,
    val runtime: String,
    val voteAverage: Double,
    val voteCount: Int,
    val popularity: Double,
    val overview: String,
    val genres: List<Genre>,
    val originalLanguage: String,
    val status: String,
    val budget: String,
    val revenue: String,
    val homepage: String?,
    val productionCompanies: List<ProductionCompany>
)

data class ProductionCompany(
    val id: Int,
    val name: String,
    val logoUrl: String?
)

data class Review(
    val id: String,
    val author: String,
    val username: String,
    val avatarUrl: String?,
    val rating: Double?,
    val content: String,
    val createdAtFormatted: String
)

data class Video(
    val id: String,
    val youtubeUrl: String,
    val key: String,
    val name: String,
    val isTrailer: Boolean
)
