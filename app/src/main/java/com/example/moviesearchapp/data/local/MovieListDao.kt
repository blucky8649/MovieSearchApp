package com.example.moviesearchapp.data.local

import androidx.room.*
import com.example.moviesearchapp.model.entities.MovieListEntity
import com.example.moviesearchapp.model.entities.MovieSavedEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<MovieListEntity>)

    @Update
    suspend fun update(item: MovieListEntity)

    @Query("SELECT * FROM MOVIE_LIST")
    fun getMovieList(): Flow<List<MovieListEntity>>

    @Query("DELETE FROM MOVIE_LIST")
    fun clear()

    @Query("SELECT COUNT(*) FROM MOVIE_LIST")
    suspend fun getCount(): Int
}