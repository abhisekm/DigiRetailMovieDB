package com.abhisekm.digiretailmoviedb.ui.main.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import androidx.paging.PagedList
import com.abhisekm.digiretailmoviedb.database.MovieDatabase
import com.abhisekm.digiretailmoviedb.database.getDatabase
import com.abhisekm.digiretailmoviedb.domain.Movie
import com.abhisekm.digiretailmoviedb.network.NetworkMoviesResponse
import com.abhisekm.digiretailmoviedb.repository.MovieRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.io.IOException

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val database = getDatabase(application)
    private val movieRepository = MovieRepository(database)

    val movies: LiveData<PagedList<Movie>> = movieRepository.movies
    val reachedEnd = movieRepository.reachedEnd

    private val _loading = MutableLiveData<Boolean>(false)
    val loading: LiveData<Boolean>
        get() = _loading

    private var hasMore: Boolean = true
    private var page = 0

    init {
        viewModelScope.launch {
            try {
                movieRepository.loadMore(page++)
            } catch (networkError: IOException) {

            }
        }
    }



    fun loadMore(): LiveData<Boolean> {
        val response = MutableLiveData<Boolean>()
        if (hasMore && _loading.value!!.not()) {
            viewModelScope.launch {
                try {
                    _loading.value = true
                    hasMore = movieRepository.loadMore(page++)
                    _loading.value = false
                    response.value = true
                } catch (networkError: IOException) {

                }
            }
        }else{
            response.value = true
        }

        return response
    }

//    private var _eventNetworkError = MutableLiveData<Event<Boolean>>()
//
//    val eventNetworkError: LiveData<Event<Boolean>>
//        get() = _eventNetworkError
//

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}