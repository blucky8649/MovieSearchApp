package com.example.moviesearchapp.util

import com.example.moviesearchapp.model.entities.MovieListEntity
import com.example.moviesearchapp.model.entities.MovieSavedEntity
import com.example.mymovieapp.model.MovieItem

fun MovieListEntity.toMovieItem(): MovieItem {
    return MovieItem(
        title = this.title,
        link = this.link,
        image = this.image,
        subtitle = this.subtitle,
        pubDate = this.pubDate,
        director = this.director,
        actor = this.actor,
        userRating = this.userRating,
        likeState = this.likeState
    )
}

fun MovieItem.toMovieListEntity(): MovieListEntity {
    return MovieListEntity(
        title = this.title,
        link = this.link,
        image = this.image,
        subtitle = this.subtitle,
        pubDate = this.pubDate,
        director = this.director,
        actor = this.actor,
        userRating = this.userRating,
        likeState = this.likeState
    )
}

fun MovieSavedEntity.toMovieItem(): MovieItem {
    return MovieItem(
        title = this.title,
        link = this.link,
        image = this.image,
        subtitle = this.subtitle,
        pubDate = this.pubDate,
        director = this.director,
        actor = this.actor,
        userRating = this.userRating,
        likeState = this.likeState
    )
}

fun MovieItem.toMovieSavedEntity(): MovieSavedEntity {
    return MovieSavedEntity(
        title = this.title,
        link = this.link,
        image = this.image,
        subtitle = this.subtitle,
        pubDate = this.pubDate,
        director = this.director,
        actor = this.actor,
        userRating = this.userRating,
        likeState = this.likeState
    )
}