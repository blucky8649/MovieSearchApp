package com.example.moviesearchapp.data.local

import androidx.room.*
import com.example.moviesearchapp.model.entities.MovieSavedEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieSaveDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun like(item: MovieSavedEntity)

    @Delete
    suspend fun unLike(item: MovieSavedEntity)

    @Query("SELECT * FROM MOVIE_SAVE")
    fun getMovieList(): Flow<List<MovieSavedEntity>>

    @Query("SELECT likeState FROM MOVIE_SAVE WHERE link = :link")
    fun getSavedState(link: String): Boolean
}