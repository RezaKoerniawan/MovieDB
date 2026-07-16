package com.example.core.di

import android.content.Context
import com.example.core.network.ApiKeyInterceptor
import com.example.core.network.MovieApiService
import com.example.data.local.FavoriteMovieDataStore
import com.example.data.repository.MovieRepositoryImpl
import com.example.domain.repository.MovieRepository
import com.example.domain.usecase.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import com.chuckerteam.chucker.api.ChuckerInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

interface AppContainer {
    val movieRepository: MovieRepository
    val favoriteMovieDataStore: FavoriteMovieDataStore
    val ioDispatcher: CoroutineDispatcher
    val mainDispatcher: CoroutineDispatcher

    val getGenresUseCase: GetGenresUseCase
    val getMoviesByGenreUseCase: GetMoviesByGenreUseCase
    val getMovieDetailsUseCase: GetMovieDetailsUseCase
    val getUserReviewsUseCase: GetUserReviewsUseCase
    val getMovieTrailersUseCase: GetMovieTrailersUseCase
    val getSimilarMoviesUseCase: GetSimilarMoviesUseCase
    val searchMoviesUseCase: SearchMoviesUseCase
    val getFavoriteMoviesUseCase: GetFavoriteMoviesUseCase
    val toggleFavoriteMovieUseCase: ToggleFavoriteMovieUseCase
    val isMovieFavoriteUseCase: IsMovieFavoriteUseCase
}

class AppContainerImpl(private val context: Context) : AppContainer {

    override val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
    override val mainDispatcher: CoroutineDispatcher = Dispatchers.Main

    // ==========================================
    // MODULE: Network & Serialization Module
    // ==========================================

    private val moshi: Moshi by lazy {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    private val okHttpClient: OkHttpClient by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val chuckerInterceptor = ChuckerInterceptor.Builder(context).build()

        OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(ApiKeyInterceptor())
            .addInterceptor(loggingInterceptor)
            .addInterceptor(chuckerInterceptor)
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    private val apiService: MovieApiService by lazy {
        retrofit.create(MovieApiService::class.java)
    }

    // ==========================================
    // MODULE: Local Database & Storage Module
    // ==========================================

    override val favoriteMovieDataStore: FavoriteMovieDataStore by lazy {
        FavoriteMovieDataStore(context)
    }

    // ==========================================
    // MODULE: Repository Module
    // ==========================================

    override val movieRepository: MovieRepository by lazy {
        MovieRepositoryImpl(
            apiService = apiService,
            favoriteDataStore = favoriteMovieDataStore,
            ioDispatcher = ioDispatcher
        )
    }

    // ==========================================
    // MODULE: Use Cases Module (Lazy Instantiation)
    // ==========================================

    override val getGenresUseCase: GetGenresUseCase by lazy {
        GetGenresUseCase(movieRepository)
    }

    override val getMoviesByGenreUseCase: GetMoviesByGenreUseCase by lazy {
        GetMoviesByGenreUseCase(movieRepository)
    }

    override val getMovieDetailsUseCase: GetMovieDetailsUseCase by lazy {
        GetMovieDetailsUseCase(movieRepository)
    }

    override val getUserReviewsUseCase: GetUserReviewsUseCase by lazy {
        GetUserReviewsUseCase(movieRepository)
    }

    override val getMovieTrailersUseCase: GetMovieTrailersUseCase by lazy {
        GetMovieTrailersUseCase(movieRepository)
    }

    override val getSimilarMoviesUseCase: GetSimilarMoviesUseCase by lazy {
        GetSimilarMoviesUseCase(movieRepository)
    }

    override val searchMoviesUseCase: SearchMoviesUseCase by lazy {
        SearchMoviesUseCase(movieRepository)
    }

    override val getFavoriteMoviesUseCase: GetFavoriteMoviesUseCase by lazy {
        GetFavoriteMoviesUseCase(movieRepository)
    }

    override val toggleFavoriteMovieUseCase: ToggleFavoriteMovieUseCase by lazy {
        ToggleFavoriteMovieUseCase(movieRepository)
    }

    override val isMovieFavoriteUseCase: IsMovieFavoriteUseCase by lazy {
        IsMovieFavoriteUseCase(movieRepository)
    }
}