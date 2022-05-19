package com.example.moviesearchapp.presenter.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviesearchapp.databinding.ItemMovieBinding
import com.example.moviesearchapp.presenter.MovieViewModel
import com.example.moviesearchapp.util.removeTags
import com.example.moviesearchapp.util.removeVerticalBarFromText
import com.example.mymovieapp.model.MovieItem

class MovieListAdapter(
    private val viewModel: MovieViewModel
): ListAdapter<MovieItem, MovieListAdapter.MovieListViewHolder>(differCallback){

    companion object {
        private val differCallback = object : DiffUtil.ItemCallback<MovieItem>() {
            override fun areItemsTheSame(oldItem: MovieItem, newItem: MovieItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: MovieItem, newItem: MovieItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    inner class MovieListViewHolder(val binding: ItemMovieBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: MovieItem) {
            binding.apply {
                tvDirectiors.text = removeVerticalBarFromText(item.director)
                tvMovieTitle.text = removeTags(item.title)
                tvRatingScore.text = item.userRating.toString()
                ratingbarSmall.rating = item.userRating.div(2).toFloat()
                tvActors.text = removeTags(item.actor)
                Glide.with(root)
                    .load(item.image)
                    .centerCrop()
                    .into(ivMovieImage)

                btnLike.apply {
                    isChecked = item.likeState
                    setOnClickListener {
                        when (item.likeState) {
                            true -> {
                                viewModel.setLikeState(
                                    item = item,
                                    state = false
                                )
                            }
                            false -> {
                                viewModel.setLikeState(
                                    item = item,
                                    state = true
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieListViewHolder {
        return MovieListViewHolder(
            ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MovieListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}