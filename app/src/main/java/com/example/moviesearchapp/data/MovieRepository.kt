package com.example.moviesearchapp.data

import com.example.mymovieapp.model.Movie
import com.example.mymovieapp.model.MovieItem
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    suspend fun like(item: MovieItem)
    fun getSavedMovieList(): Flow<List<MovieItem>>
    suspend fun unLike(item: MovieItem)
    suspend fun getLikeState(keyword: String) : Boolean
    suspend fun getRemoteMovieList(searchQuery: String, start: Int): Movie
    suspend fun clear()
    suspend fun insertAll(items: List<MovieItem>)
    fun getMovieListFlow(): Flow<List<MovieItem>>
    suspend fun getCount(): Int
}