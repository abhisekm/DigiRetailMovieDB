package com.abhisekm.digiretailmoviedb.database

import androidx.room.*
import com.abhisekm.digiretailmoviedb.domain.Movie


@Entity(tableName = "movies_table")
data class DatabaseMovies(
    @PrimaryKey
    var id: Int,

    @ColumnInfo(name = "poster_path")
    var posterPath: String,

    @ColumnInfo(name = "backdrop_path")
    var backdropPath: String,

    var title: String,

    @ColumnInfo(name = "release_date")
    var releaseDate: String,

    @ColumnInfo(name = "vote_average")
    var voteAverage: Double,

    var overview: String,
)



fun DatabaseMovies.asDomainModel(): Movie {
    return Movie(
        id,
        posterPath,
        backdropPath,
        title,
        releaseDate,
        voteAverage,
        overview
    )
}

fun List<DatabaseMovies>.asDomainModel(): List<Movie> {
    return map {
        Movie(
            it.id,
            it.posterPath,
            it.backdropPath,
            it.title,
            it.releaseDate,
            it.voteAverage,
            it.overview
        )
    }
}