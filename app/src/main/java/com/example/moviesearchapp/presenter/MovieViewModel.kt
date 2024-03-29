package com.example.moviesearchapp.presenter

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesearchapp.data.MovieRepository
import com.example.moviesearchapp.util.Resource
import com.example.moviesearchapp.model.MovieItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val repository: MovieRepository
): ViewModel() {
    private val _searchQuery: MutableStateFlow<String> = MutableStateFlow("")
    val searchQuery get() = _searchQuery as StateFlow<String>

    fun postKeyword(searchQuery: String) {
        _searchQuery.value = searchQuery
    }

    private var _movieState = MutableStateFlow<Resource<List<MovieItem>>>(Resource.Loading())
    val movieState get() = _movieState as StateFlow<Resource<List<MovieItem>>>

    var currentPageNum = 1

    private val _isEndReached = MutableStateFlow(false)
    val isEndReached get() = _isEndReached as StateFlow<Boolean>

    init {
        getData()
        viewModelScope.launch {
            searchQuery.collectLatest {
                handleProductList(it, true)
            }
        }

    }

    fun fetchFavoriteMovieList() = repository.getSavedMovieList()

    fun handleProductList(
        searchQuery: String = this.searchQuery.value,
        isRefresh: Boolean
    ) = CoroutineScope(Dispatchers.IO).launch {
        _movieState.emit(Resource.Loading())
        if (isRefresh) {
            repository.clear()
            currentPageNum = 1
            _isEndReached.emit(false)
        }
        if(isEndReached.value) {
            _movieState.emit(Resource.Error("더 이상 불러올 데이터가 없습니다."))
            return@launch
        }


        val response = repository.getRemoteMovieList(searchQuery, currentPageNum)

        if (response.isSuccessful) {
            response.body()?.let {
                if (it.items.isEmpty()) {
                    _movieState.emit(Resource.Error("데이터가 없습니다."))
                    return@launch
                }
                currentPageNum += it.items.size
                val cachedList = it.items.map { movieItem ->
                    val state = async { repository.getLikeState(movieItem.link) }
                    movieItem.copy(likeState = state.await())
                }.toList()
                cachedList.let { repository.insertAll(it) }
                val count = repository.getCount()

                if (count >= it.total) {
                    _isEndReached.emit(true)
                }
            }
        } else {
            _movieState.emit(Resource.Error("오류가 발생하였습니다."))
        }
    }
    fun getData() = viewModelScope.launch {
        repository.getMovieListFlow().collectLatest { movieList ->
            _movieState.emit(Resource.Success(movieList))
        }
    }

    fun setLikeState(item: MovieItem, state: Boolean) = viewModelScope.launch {
        when(state) {
            true -> repository.like(item.copy(likeState = state))
            false -> repository.unLike(item.copy(likeState = state))
        }
    }
}