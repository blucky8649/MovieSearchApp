package com.example.moviesearchapp.presenter.MovieFavoriteFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviesearchapp.databinding.FragmentMovieListBinding
import com.example.moviesearchapp.databinding.FragmentRecyclerviewOnlyBinding
import com.example.moviesearchapp.presenter.MovieViewModel
import com.example.moviesearchapp.presenter.adapters.MovieListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieFavoriteFragment: Fragment() {
    private var _binding: FragmentRecyclerviewOnlyBinding? = null
    private val binding get() = _binding!!
    private val movieAdapter by lazy {
        MovieListAdapter(viewModel)
    }
    private val viewModel: MovieViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecyclerviewOnlyBinding.inflate(layoutInflater, container, false)
        setupRecyclerView()
        collectFavoriteState()
        return binding.root
    }
    private fun collectFavoriteState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.fetchFavoriteMovieList().collectLatest {
                movieAdapter.submitList(it.toList())
            }
        }
    }
    private fun setupRecyclerView() {
        binding.rvList.apply {
            adapter = movieAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}