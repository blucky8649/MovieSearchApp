package com.example.moviesearchapp.data.repository

import com.example.moviesearchapp.data.MovieRepository
import com.example.moviesearchapp.data.local.MovieDatabase
import com.example.moviesearchapp.data.remote.MovieApi
import com.example.moviesearchapp.util.toMovieItem
import com.example.moviesearchapp.util.toMovieListEntity
import com.example.moviesearchapp.util.toMovieSavedEntity
import com.example.mymovieapp.model.Movie
import com.example.moviesearchapp.model.MovieItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class MovieRepositoryImpl(
    private val db: MovieDatabase,
    private val api: MovieApi,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : MovieRepository {
    override suspend fun like(item: MovieItem) {
        withContext(ioDispatcher) {
            launch { db.listDao.update(item.toMovieListEntity()) }
            launch { db.saveDao.like(item.toMovieSavedEntity()) }
        }
    }

    override fun getSavedMovieList(): Flow<List<MovieItem>> {
        return db.saveDao.getMovieList().map { list ->
            list.map { movieSavedEntity ->
                movieSavedEntity.toMovieItem()
            }
        }
    }

    override suspend fun unLike(item: MovieItem) {
        withContext(ioDispatcher) {
            launch { db.listDao.update(item.toMovieListEntity()) }
            launch { db.saveDao.unLike(item.toMovieSavedEntity()) }
        }
    }

    override suspend fun getLikeState(keyword: String) : Boolean {
        return db.saveDao.getSavedState(keyword)
    }

    override suspend fun getRemoteMovieList(searchQuery: String, start: Int): Response<Movie> {
        return api.getMovieList(query = searchQuery, start = start)
    }

    override suspend fun clear() {
        db.listDao.clear()
    }

    override suspend fun insertAll(items: List<MovieItem>) {
        db.listDao.insertAll(items.map { it.toMovieListEntity() })
    }

    override fun getMovieListFlow(): Flow<List<MovieItem>> {
        return db.listDao.getMovieList().map { list ->
            list.map { movieListEntity ->
                movieListEntity.toMovieItem()
            }
        }
    }

    override suspend fun getCount(): Int {
        return db.listDao.getCount()
    }
}