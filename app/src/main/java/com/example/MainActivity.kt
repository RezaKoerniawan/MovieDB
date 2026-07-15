package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.core.ui.ViewModelFactory
import com.example.presentation.detail.DetailScreen
import com.example.presentation.detail.DetailViewModel
import com.example.presentation.favorite.FavoriteScreen
import com.example.presentation.favorite.FavoriteViewModel
import com.example.presentation.genre.GenreScreen
import com.example.presentation.genre.GenreViewModel
import com.example.presentation.movie_list.MovieListScreen
import com.example.presentation.movie_list.MovieListViewModel
import com.example.presentation.navigation.Screen
import com.example.presentation.search.SearchScreen
import com.example.presentation.search.SearchViewModel
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    MovieAppNavigation()
                }
            }
        }
    }
}

@Composable
fun MovieAppNavigation() {
    val navController = rememberNavController()
    
    // Mengambil AppContainer dari ApplicationContext untuk manual dependency injection
    val context = LocalContext.current
    val appContainer = (context.applicationContext as MovieApplication).container
    val factory = ViewModelFactory(appContainer)

    NavHost(
        navController = navController,
        startDestination = Screen.Genre.route
    ) {
        // 1. Genre Screen (Home / Discovery)
        composable(Screen.Genre.route) {
            val viewModel: GenreViewModel = viewModel(factory = factory)
            GenreScreen(
                viewModel = viewModel,
                onGenreClick = { genre ->
                    navController.navigate(Screen.MovieList.createRoute(genre.id, genre.name))
                },
                onSearchClick = {
                    navController.navigate(Screen.Search.route)
                },
                onFavoritesClick = {
                    navController.navigate(Screen.Favorite.route)
                }
            )
        }

        // 2. Movie List Screen (Endless scroll berdasarkan genre)
        composable(
            route = Screen.MovieList.route,
            arguments = listOf(
                navArgument("genreId") { type = NavType.IntType },
                navArgument("genreName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val genreId = backStackEntry.arguments?.getInt("genreId") ?: 0
            val genreName = backStackEntry.arguments?.getString("genreName") ?: ""
            val viewModel: MovieListViewModel = viewModel(factory = factory)

            MovieListScreen(
                genreId = genreId,
                genreName = genreName,
                viewModel = viewModel,
                onMovieClick = { movie ->
                    navController.navigate(Screen.Detail.createRoute(movie.id))
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        // 3. Movie Detail Screen (Rincian, Trailer, Ulasan, Film Mirip)
        composable(
            route = Screen.Detail.route,
            arguments = listOf(
                navArgument("movieId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId") ?: 0
            val viewModel: DetailViewModel = viewModel(factory = factory)

            DetailScreen(
                movieId = movieId,
                viewModel = viewModel,
                onMovieClick = { movie ->
                    // Navigasi rekursif ke film serupa
                    navController.navigate(Screen.Detail.createRoute(movie.id))
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        // 4. Favorites Screen (Offline saved)
        composable(Screen.Favorite.route) {
            val viewModel: FavoriteViewModel = viewModel(factory = factory)
            FavoriteScreen(
                viewModel = viewModel,
                onMovieClick = { movie ->
                    navController.navigate(Screen.Detail.createRoute(movie.id))
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        // 5. Search Screen (Real-time dynamic search)
        composable(Screen.Search.route) {
            val viewModel: SearchViewModel = viewModel(factory = factory)
            SearchScreen(
                viewModel = viewModel,
                onMovieClick = { movie ->
                    navController.navigate(Screen.Detail.createRoute(movie.id))
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
