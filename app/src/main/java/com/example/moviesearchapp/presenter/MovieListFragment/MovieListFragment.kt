package com.example.moviesearchapp.presenter.MovieListFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesearchapp.databinding.FragmentMovieListBinding
import com.example.moviesearchapp.presenter.MovieViewModel
import com.example.moviesearchapp.presenter.adapters.MovieListAdapter
import com.example.moviesearchapp.util.Constants.PAGE_SIZE
import com.example.moviesearchapp.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieListFragment: Fragment() {
    private var _binding: FragmentMovieListBinding? = null
    private val binding get()= _binding!!

    val viewModel: MovieViewModel by activityViewModels()
    private val movieAdapter by lazy {
        MovieListAdapter(viewModel)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMovieListBinding.inflate(layoutInflater, container, false)
        setUpRecyclerView()
        setupSwipeRefresh()
        collectMovieItemState()
        collectEndPageState()

        return binding.root
    }
    private fun collectEndPageState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isEndReached.collectLatest { isEndReached ->
                isLastPage = isEndReached
            }
        }
    }
    private fun collectMovieItemState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.movieState.collectLatest { result ->
                when(result) {
                    is Resource.Success -> {
                        result.data?.let {
                            hideProgressBar()
                            movieAdapter.submitList(it.toList())
                        }
                    }
                    is Resource.Error -> {
                        hideProgressBar()
                        Toast.makeText(activity, "${result.message}", Toast.LENGTH_LONG).show()
                    }
                    is Resource.Loading -> {
                        showProgressBar()
                    }
                }
            }
        }
    }
    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.apply {
            setOnRefreshListener {
                viewModel.handleProductList(viewModel.searchQuery.value, true)
                binding.recyclerView.setPadding(0, 0, 0, 200)
                isRefreshing = false
            }
        }
    }
    private fun setUpRecyclerView() {
        binding.recyclerView.apply {
            adapter = movieAdapter
            layoutManager = LinearLayoutManager(activity)
            itemAnimator = null
            addOnScrollListener(scrollListener)
            setPadding(0, 0, 0, 200)

        }
    }
    private fun showProgressBar() {
        binding.progressBarMain.isVisible = true
        binding.progressBar.isVisible = true
        isLoading = true
    }
    private fun hideProgressBar() {
        binding.progressBar.isVisible = false
        binding.progressBarMain.isVisible = false
        isLoading = false
    }
    private var isLoading = false
    private var isLastPage = false
    private var isScrolling = false
    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                isScrolling = true
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndIsNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= PAGE_SIZE
            val shouldPaginate = isNotLoadingAndIsNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling

            if (isAtLastItem && isLastPage) {
                // 아이템이 전부 로드된 상태에서 스크롤 포지션이 리사이클러뷰 마지막이라면 패딩을 좁힌다.
                binding.recyclerView.setPadding(0, 0, 0, 0)
            }
            if (shouldPaginate) {
                viewModel.handleProductList(viewModel.searchQuery.value, false)
                isScrolling = false
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}