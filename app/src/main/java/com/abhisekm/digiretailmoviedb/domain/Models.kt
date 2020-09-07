package com.abhisekm.digiretailmoviedb.domain

data class Movie(
    var id: Int,
    var posterPath: String,
    var backdropPath: String,
    var title: String,
    var releaseDate: String,
    var voteAverage: Double,
    var overview: String
)


data class MovieDetails(
    var id: Int,
    var posterPath: String?,
    var backdropPath: String?,
    var title: String?,
    var releaseDate: String?,
    var voteAverage: Double?,
    var genres: List<String>?,
    var overview: String?,
    var imdbId: String?
)
