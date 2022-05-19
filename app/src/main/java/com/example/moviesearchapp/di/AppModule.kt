package com.example.moviesearchapp.di

import android.app.Application
import androidx.room.Room
import com.example.moviesearchapp.data.MovieRepository
import com.example.moviesearchapp.data.local.MovieDatabase
import com.example.moviesearchapp.data.remote.MovieApi
import com.example.moviesearchapp.data.repository.MovieRepositoryImpl
import com.example.moviesearchapp.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideRemoteApi() : MovieApi {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(MovieApi::class.java)
    }
    @Provides
    @Singleton
    fun provideRoomDatabase(app: Application): MovieDatabase =
        Room.databaseBuilder(
            app,
            MovieDatabase::class.java,
            "movie_db.db"
        ).build()

    @Provides
    @Singleton
    fun provideMovieRepository(api: MovieApi, db: MovieDatabase): MovieRepository = MovieRepositoryImpl(db, api)
}