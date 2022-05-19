package com.example.moviesearchapp.data.local

import androidx.room.Database
import com.example.moviesearchapp.model.entities.MovieListEntity
import com.example.moviesearchapp.model.entities.MovieSavedEntity

@Database(
    entities = [MovieSavedEntity::class, MovieListEntity::class],
    version = 1
)
abstract class MovieDatabase {
    abstract val saveDao: MovieSaveDao
    abstract val listDao: MovieListDao
}