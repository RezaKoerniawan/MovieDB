# MovieDB

Movie discovery Android application built with Kotlin and Jetpack Compose. It demonstrates a layered MVVM architecture, paginated TMDB results, local favorites, and dependency injection with Dagger Hilt.

## Features

- Browse movie genres and discover movies by genre.
- View movie details, trailers, reviews, and similar titles.
- Search movies with a debounced query and Paging 3.
- Save and remove favorite movies locally with Preferences DataStore.
- Render loading, success, and error states in the UI.

## Technology stack

| Area | Technology |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose, Material 3 |
| Navigation | Navigation Compose |
| Presentation | ViewModel, StateFlow, Coroutines |
| Architecture | MVVM with Clean Architecture-inspired layers |
| Dependency injection | Dagger Hilt |
| Networking | Retrofit, OkHttp |
| Serialization | Moshi |
| Pagination | Paging 3 |
| Local persistence | Preferences DataStore |
| Image loading | Coil |
| Tests | JUnit, MockK, Turbine, Robolectric, Roborazzi |
| API | The Movie Database (TMDB) |

## Architecture

The application separates presentation, domain, and data responsibilities:

```text
Compose UI
   |
Hilt ViewModel
   |
Use Cases
   |
MovieRepository (domain contract)
   |
MovieRepositoryImpl (data implementation)
   |                         |
Retrofit / TMDB API     Preferences DataStore
```

- **Presentation:** Compose screens render state exposed by ViewModels.
- **Domain:** use cases and repository contracts express application behavior without depending on Android UI or networking implementations.
- **Data:** repository implementation coordinates remote API calls, Paging sources, DTO mapping, and local favorites.
- **Dependency injection:** Hilt constructs the dependency graph. Constructor injection is used for ViewModels, use cases, repositories, and local storage. Hilt modules provide third-party infrastructure types and bind repository interfaces.

## Dependency injection

- `MovieApplication` is annotated with `@HiltAndroidApp`.
- `MainActivity` is annotated with `@AndroidEntryPoint`.
- ViewModels use `@HiltViewModel` and `@Inject constructor`.
- `RepositoryModule` binds `MovieRepositoryImpl` to `MovieRepository` using `@Binds`.
- `NetworkModule` provides singleton Moshi, OkHttp, Retrofit, and `MovieApiService`.
- `DispatcherModule` provides a qualified IO dispatcher.
- The previous manual `AppContainer` and custom `ViewModelFactory` are no longer required.

## Local setup

### Requirements

- Android Studio with a compatible Android SDK.
- JDK 17.
- A TMDB API key.

### Configure TMDB

1. Create an API key from your TMDB account.
2. Copy `.env.example` to `.env` in the project root.
3. Replace the placeholder:

   ```properties
   TMDB_API_KEY=your_actual_tmdb_api_key
   ```

4. Keep `.env` out of version control. The repository's `.gitignore` excludes it.

The Secrets Gradle Plugin reads `.env` and falls back to `.env.example`. The placeholder allows Gradle configuration to proceed, but API requests will fail until a real key is configured locally.

### Build and test

macOS / Linux:

```bash
./gradlew :app:assembleDebug
./gradlew :app:testDebugUnitTest
```

Windows:

```bat
gradlew.bat :app:assembleDebug
gradlew.bat :app:testDebugUnitTest
```

## Security notes

- Never commit `.env`, `local.properties`, signing keys, or keystore files.
- A key embedded in an Android application can be extracted from the APK. Treat client-side API keys as public-facing credentials and apply provider-side restrictions where available. A backend proxy is preferable for privileged credentials.
- HTTP logging and Chucker inspection are enabled only in debug builds. They are installed before the API-key interceptor so the key is not included in the captured request URL.
- If a previously committed TMDB key is active, revoke or rotate it. Replacing the value in `.env.example` does not remove it from Git history.

## Suggested demo flow

1. Open the genre discovery screen.
2. Select a genre and scroll through the paginated movie list.
3. Open a movie to show details, trailer, reviews, and similar movies.
4. Save the movie to favorites, then open the favorites screen.
5. Search for a title and demonstrate debounced paginated results.

## Follow-up improvements

This project is a technical demonstration and is not a claim of production readiness. Potential next steps include broader ViewModel and repository tests, DataStore persistence tests, CI build verification, release minification, and a backend strategy for protecting API credentials.
