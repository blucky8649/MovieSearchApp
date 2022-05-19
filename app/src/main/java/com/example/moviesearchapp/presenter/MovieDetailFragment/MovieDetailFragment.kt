package com.example.moviesearchapp.presenter.MovieDetailFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.moviesearchapp.databinding.FragmentWebviewBinding
import com.example.moviesearchapp.presenter.MovieViewModel
import com.example.moviesearchapp.util.Constants.TYPE_MOVIE_ITEM
import com.example.moviesearchapp.util.Constants.TYPE_WEB_VIEW
import com.example.moviesearchapp.util.removeVerticalBarFromText
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailFragment: Fragment() {
    private var _binding: FragmentWebviewBinding? = null
    private val binding get() = _binding!!

    private val viewModel:MovieViewModel by activityViewModels()

    val args: MovieDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWebviewBinding.inflate(layoutInflater, container, false)
        val movieItem = args.movieItem

        binding.item.apply {
            tvDirectiors.text = removeVerticalBarFromText(movieItem.director)
            tvMovieTitle.isVisible = false
            tvRatingScore.text = movieItem.userRating.toString()
            ratingbarSmall.rating = movieItem.userRating.div(2).toFloat()
            tvActors.text = removeVerticalBarFromText(movieItem.actor)
            Glide.with(root)
                .load(movieItem.image)
                .centerCrop()
                .into(ivMovieImage)
            layoutActors.isVisible = tvActors.text.isNotEmpty()
            layoutDirectors.isVisible = tvDirectiors.text.isNotEmpty()
            btnLike.apply {
                isChecked = movieItem.likeState
                setOnClickListener {
                    when (movieItem.likeState) {
                        true -> {
                            viewModel.setLikeState(
                                item = movieItem,
                                state = false
                            )
                        }
                        false -> {
                            viewModel.setLikeState(
                                item = movieItem,
                                state = true
                            )
                        }
                    }
                }
            }
        }
        binding.webViewDetail.apply {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            loadUrl(movieItem.link)
        }


        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}