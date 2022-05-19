package com.example.moviesearchapp.data.remote

import com.example.moviesearchapp.util.Constants.NAVER_ID
import com.example.moviesearchapp.util.Constants.NAVER_SECRET
import com.example.moviesearchapp.util.Constants.PAGE_SIZE
import com.example.mymovieapp.model.Movie
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface MovieApi {
    @GET("v1/search/movie.json")
    suspend fun getMovieList(
        @Header("X-Naver-Client-Id")
        clientId: String = NAVER_ID,
        @Header("X-Naver-Client-Secret")
        clientSecret: String = NAVER_SECRET,
        @Query("query")
        query: String,
        @Query("display")
        display: Int? = PAGE_SIZE,
        @Query("start")
        start: Int
    ): Movie
}