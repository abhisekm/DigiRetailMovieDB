package com.abhisekm.digiretailmoviedb.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.abhisekm.digiretailmoviedb.BuildConfig
import com.abhisekm.digiretailmoviedb.database.DatabaseMovies
import com.abhisekm.digiretailmoviedb.database.MovieDatabase
import com.abhisekm.digiretailmoviedb.database.asDomainModel
import com.abhisekm.digiretailmoviedb.domain.Movie
import com.abhisekm.digiretailmoviedb.domain.MovieDetails
import com.abhisekm.digiretailmoviedb.network.Network
import com.abhisekm.digiretailmoviedb.network.asDatabaseModel
import com.abhisekm.digiretailmoviedb.network.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieRepository(private val database: MovieDatabase) {
    private val _reachedEnd = MutableLiveData<Boolean>()
    val reachedEnd: LiveData<Boolean>
        get() = _reachedEnd

    /**
     * A list of lessons that can be shown on the screen.
     */
    val movies: LiveData<PagedList<Movie>> by lazy {
        val dataSourceFactory = database.movieDao.getMoviesPaged().map { it -> it.asDomainModel() }
        val config = PagedList.Config.Builder()
            .setPageSize(20)
            .build()
        LivePagedListBuilder(dataSourceFactory, config)
            .setBoundaryCallback(object : PagedList.BoundaryCallback<Movie>() {
                override fun onItemAtEndLoaded(itemAtEnd: Movie) {
                    super.onItemAtEndLoaded(itemAtEnd)
                    _reachedEnd.value = true
                }
            })
            .build()
    }

//    val movies: LiveData<List<Movie>> = Transformations.map(database.movieDao.getMovies()) {
//        it.asDomainModel()
//    }


    private var _movieDetails = MutableLiveData<MovieDetails>()

    val movieDetails: LiveData<MovieDetails>
        get() = _movieDetails


    /**
     * Refresh the lessons stored in the offline cache.
     */
//    suspend fun refreshVideos() {
//        withContext(Dispatchers.IO) {
//            val movies = Network.service.getTopRated(BuildConfig.API_KEY)
//            database.movieDao.insertAll(*movies.asDatabaseModel())
//        }
//    }

    suspend fun loadMore(page: Int): Boolean {
        return withContext(Dispatchers.IO) {
            val movies = Network.service.getTopRated(BuildConfig.API_KEY, page + 1)
            database.movieDao.insertAll(*movies.asDatabaseModel())
            withContext(Dispatchers.Main){
                _reachedEnd.value = false
            }
            return@withContext movies.page <= movies.page
        }
    }


    suspend fun getMovieDetails(id: Int) {
        withContext(Dispatchers.IO) {
            val details = Network.service.getMovieDetails(id, BuildConfig.API_KEY)
            _movieDetails.value = details.asDomainModel()
        }
    }


}