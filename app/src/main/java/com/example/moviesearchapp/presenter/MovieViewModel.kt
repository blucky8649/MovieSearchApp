package com.example.moviesearchapp.presenter

import com.example.moviesearchapp.data.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    val repository: MovieRepository
) {

}