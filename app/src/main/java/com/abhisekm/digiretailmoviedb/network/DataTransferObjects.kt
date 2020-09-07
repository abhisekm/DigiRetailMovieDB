package com.abhisekm.digiretailmoviedb.network

import com.abhisekm.digiretailmoviedb.database.DatabaseMovies
import com.abhisekm.digiretailmoviedb.domain.Movie
import com.abhisekm.digiretailmoviedb.domain.MovieDetails
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkMoviesResponse(
    val page: Int,
    @Json(name = "total_results") val totalResults: Int,
    @Json(name = "total_pages") val totalPages: Int,
    val results: List<NetworkMovies>
)

@JsonClass(generateAdapter = true)
data class NetworkMovies(
    val id: Int,
    @Json(name = "poster_path") val posterPath: String?,
    @Json(name = "backdrop_path") val backdropPath: String?,
    val title: String?,
    @Json(name = "release_date") val releaseDate: String?,
    @Json(name = "vote_average") val voteAverage: Double,
    val overview: String?,
)

@JsonClass(generateAdapter = true)
data class NetworkMovieDetails(
    val id: Int,
    @Json(name = "poster_path") val posterPath: String?,
    @Json(name = "backdrop_path") val backdropPath: String?,
    val title: String?,
    @Json(name = "release_date") val releaseDate: String?,
    @Json(name = "vote_average") val voteAverage: Double,
    val genres: List<NetworkGenres>?,
    val overview: String?,
    @Json(name = "imdb_id") val imdbId: String?
)

@JsonClass(generateAdapter = true)
data class NetworkGenres(
    val id: Int,
    val name: String,
)

/**
 * Convert network to domain objects. Encapsulate domain from network
 */
fun NetworkMovies.asDomainModel(): Movie {
    return Movie(
        id,
        posterPath ?: "",
        backdropPath ?: "",
        title ?: "",
        releaseDate ?:  "",
        voteAverage,
        overview ?: ""
    )
}

fun NetworkMovieDetails.asDomainModel(): MovieDetails {
    val genreList: List<String> = genres?.map { it.name } ?: listOf()

    return MovieDetails(
        id,
        posterPath,
        backdropPath,
        title,
        releaseDate,
        voteAverage,
        genreList,
        overview,
        imdbId
    )
}

/**
 * Convert network to database objects. Encapsulate database from network
 */
fun NetworkMoviesResponse.asDatabaseModel(): Array<DatabaseMovies> {
    return results.map {
        DatabaseMovies(
            it.id,
            it.posterPath ?: "",
            it.backdropPath ?: "",
            it.title ?: "",
            it.releaseDate ?: "",
            it.voteAverage,
            it.overview ?: ""
        )
    }.toTypedArray()
}