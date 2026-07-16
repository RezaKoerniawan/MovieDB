package com.example.data.mapper

import com.example.data.model.*
import com.example.domain.model.*
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*


fun GenreDto.toDomain(): Genre {
    return Genre(id = id, name = name)
}

fun MovieDto.toDomain(): Movie {
    val posterUrl = if (!posterPath.isNullOrEmpty()) "https://image.tmdb.org/t/p/w500$posterPath" else null
    val backdropUrl = if (!backdropPath.isNullOrEmpty()) "https://image.tmdb.org/t/p/w780$backdropPath" else null
    return Movie(
        id = id,
        title = title,
        originalTitle = originalTitle ?: title,
        posterUrl = posterUrl,
        backdropUrl = backdropUrl,
        releaseDate = formatDateString(releaseDate),
        voteAverage = voteAverage,
        voteCount = voteCount,
        overview = overview,
        popularity = popularity
    )
}

fun MovieDetailsResponseDto.toDomain(): MovieDetails {
    val posterUrl = if (!posterPath.isNullOrEmpty()) "https://image.tmdb.org/t/p/w500$posterPath" else null
    val backdropUrl = if (!backdropPath.isNullOrEmpty()) "https://image.tmdb.org/t/p/w780$backdropPath" else null
    
    val formattedRuntime = runtime?.let {
        val hrs = it / 60
        val mins = it % 60
        if (hrs > 0) "${hrs}h ${mins}m" else "${mins}m"
    } ?: "N/A"

    val formattedBudget = if (budget != null && budget > 0) {
        NumberFormat.getCurrencyInstance(Locale.US).format(budget).substringBefore(".")
    } else {
        "N/A"
    }

    val formattedRevenue = if (revenue != null && revenue > 0) {
        NumberFormat.getCurrencyInstance(Locale.US).format(revenue).substringBefore(".")
    } else {
        "N/A"
    }

    return MovieDetails(
        id = id,
        title = title,
        originalTitle = originalTitle ?: title,
        posterUrl = posterUrl,
        backdropUrl = backdropUrl,
        releaseDate = formatDateString(releaseDate),
        runtime = formattedRuntime,
        voteAverage = voteAverage,
        voteCount = voteCount,
        popularity = popularity,
        overview = overview,
        genres = genres?.map { it.toDomain() } ?: emptyList(),
        originalLanguage = originalLanguage?.uppercase(Locale.getDefault()) ?: "N/A",
        status = status ?: "Unknown",
        budget = formattedBudget,
        revenue = formattedRevenue,
        homepage = homepage,
        productionCompanies = productionCompanies?.map { it.toDomain() } ?: emptyList()
    )
}

fun ProductionCompanyDto.toDomain(): ProductionCompany {
    val logoUrl = if (!logoPath.isNullOrEmpty()) "https://image.tmdb.org/t/p/w200$logoPath" else null
    return ProductionCompany(
        id = id,
        name = name,
        logoUrl = logoUrl
    )
}

fun ReviewDto.toDomain(): Review {
    // TMDB avatar path bisa berupa URL lengkap (jika login pihak ketiga) atau partial path
    val rawAvatar = authorDetails?.avatarPath
    val avatarUrl = when {
        rawAvatar.isNullOrEmpty() -> null
        rawAvatar.startsWith("/https") -> rawAvatar.substring(1)
        rawAvatar.startsWith("http") -> rawAvatar
        else -> "https://image.tmdb.org/t/p/w200$rawAvatar"
    }

    return Review(
        id = id,
        author = author,
        username = authorDetails?.username ?: author,
        avatarUrl = avatarUrl,
        rating = authorDetails?.rating,
        content = content,
        createdAtFormatted = formatDateStringWithTime(createdAt)
    )
}

fun VideoDto.toDomain(): Video {
    return Video(
        id = id,
        youtubeUrl = "https://www.youtube.com/watch?v=$key",
        key = key,
        name = name,
        isTrailer = type.equals("Trailer", ignoreCase = true) && site.equals("YouTube", ignoreCase = true)
    )
}

private fun formatDateString(dateStr: String?): String {
    if (dateStr.isNullOrEmpty()) return "Unknown Release Date"
    return try {
        val parser = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val date = parser.parse(dateStr)
        val formatter = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID")) // Format bahasa indonesia
        date?.let { formatter.format(it) } ?: dateStr
    } catch (e: Exception) {
        dateStr
    }
}

private fun formatDateStringWithTime(dateStr: String?): String {
    if (dateStr.isNullOrEmpty()) return "Unknown Date"
    return try {
        // format ISO, misal "2021-08-28T14:48:00.000Z" atau "2021-08-28T14:48:00Z"
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
        // Bersihkan string jika ada milidetik panjang
        val cleanedStr = dateStr.substringBefore(".")
        val date = parser.parse(cleanedStr)
        val formatter = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id", "ID"))
        date?.let { formatter.format(it) } ?: dateStr
    } catch (e: Exception) {
        dateStr
    }
}
