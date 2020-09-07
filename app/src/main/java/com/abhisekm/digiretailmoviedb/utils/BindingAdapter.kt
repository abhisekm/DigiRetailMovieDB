package com.abhisekm.digiretailmoviedb.utils

import android.widget.ImageView
import android.widget.RatingBar
import androidx.databinding.BindingAdapter
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import com.abhisekm.digiretailmoviedb.domain.Movie
import com.abhisekm.digiretailmoviedb.ui.main.adapter.MovieAdapter
import com.bumptech.glide.Glide

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: PagedList<Movie>?) {
    val adapter = recyclerView.adapter as MovieAdapter
    adapter.submitList(data)
}

@BindingAdapter("imageUrl")
fun loadImage(imageView: ImageView?, url: String?) {
    imageView?.let{ view -> url.let {image -> Glide.with(view).load(image).into(view) }}
}

@BindingAdapter("tmdbImageUrl")
fun loadTmdbImage(imageView: ImageView, path: String) {
    val url: String = "https://image.tmdb.org/t/p/w780$path"
    Glide.with(imageView).load(url).into(imageView)
}

@BindingAdapter("scaledRating")
fun scaledRating(ratingBar: RatingBar, rating: Double) {
    val scaledRating = rating / 2
    ratingBar.rating = scaledRating.toFloat()
}