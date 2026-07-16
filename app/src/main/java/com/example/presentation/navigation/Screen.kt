package com.example.presentation.navigation

sealed class Screen(val route: String) {
    object Genre : Screen("genre_screen")
    object MovieList : Screen("movie_list_screen/{genreId}/{genreName}") {
        fun createRoute(genreId: Int, genreName: String) = "movie_list_screen/$genreId/$genreName"
    }
    object Detail : Screen("detail_screen/{movieId}") {
        fun createRoute(movieId: Int) = "detail_screen/$movieId"
    }
    object Favorite : Screen("favorite_screen")
    object Search : Screen("search_screen")
}
