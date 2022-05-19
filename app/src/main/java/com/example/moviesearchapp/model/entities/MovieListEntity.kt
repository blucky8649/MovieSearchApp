package com.example.moviesearchapp.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "MOVIE_LIST"
)
class MovieListEntity(
    val title: String,
    @PrimaryKey
    val link: String,
    val image: String,
    val subtitle: String,
    val pubDate: String,
    val director: String,
    val actor: String,
    val userRating: Double = 0.0,
    val likeState: Boolean = false
)