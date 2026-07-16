package com.example.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.data.model.MovieDto
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.favoriteDataStore: DataStore<Preferences> by preferencesDataStore(name = "favorite_movies")

class FavoriteMovieDataStore(private val context: Context) {

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
        
    private val listType = Types.newParameterizedType(List::class.java, MovieDto::class.java)
    private val jsonAdapter = moshi.adapter<List<MovieDto>>(listType)

    companion object {
        private val FAVORITE_MOVIES_JSON = stringPreferencesKey("favorite_movies_json")
    }

    val favoriteMoviesFlow: Flow<List<MovieDto>> = context.favoriteDataStore.data
        .map { preferences ->
            val jsonString = preferences[FAVORITE_MOVIES_JSON]
            if (!jsonString.isNullOrEmpty()) {
                try {
                    jsonAdapter.fromJson(jsonString) ?: emptyList()
                } catch (e: Exception) {
                    emptyList()
                }
            } else {
                emptyList()
            }
        }


    suspend fun toggleFavorite(movie: MovieDto) {
        context.favoriteDataStore.edit { preferences ->
            val jsonString = preferences[FAVORITE_MOVIES_JSON]
            val currentList = if (!jsonString.isNullOrEmpty()) {
                try {
                    jsonAdapter.fromJson(jsonString)?.toMutableList() ?: mutableListOf()
                } catch (e: Exception) {
                    mutableListOf()
                }
            } else {
                mutableListOf()
            }

            val exists = currentList.any { it.id == movie.id }
            if (exists) {
                currentList.removeAll { it.id == movie.id }
            } else {
                currentList.add(movie)
            }

            preferences[FAVORITE_MOVIES_JSON] = jsonAdapter.toJson(currentList)
        }
    }
}
