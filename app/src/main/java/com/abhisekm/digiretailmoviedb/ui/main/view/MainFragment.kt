package com.abhisekm.digiretailmoviedb.ui.main.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.abhisekm.digiretailmoviedb.R
import com.abhisekm.digiretailmoviedb.databinding.MainFragmentBinding
import com.abhisekm.digiretailmoviedb.ui.main.adapter.MovieAdapter
import com.abhisekm.digiretailmoviedb.ui.main.viewmodel.MainViewModel
import com.abhisekm.digiretailmoviedb.utils.RecyclerViewLoadMoreScroll

class MainFragment : Fragment() {
    private lateinit var recycleViewLoadMoreScroll: RecyclerViewLoadMoreScroll
    private lateinit var binding: MainFragmentBinding
    private val viewModel: MainViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProvider(this, MainViewModel.Factory(activity.application))
            .get(MainViewModel::class.java)
    }

    private var isLoading = false
    private var hasLoadedAllItems = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(layoutInflater, R.layout.main_fragment, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val pagedMovieAdapter = MovieAdapter(MovieAdapter.OnClickListener {
//            viewModel.joinClassroom(it)
        })
        val layoutManager = LinearLayoutManager(context)
        binding.movieList.layoutManager = layoutManager
        binding.movieList.adapter = pagedMovieAdapter
        viewModel.movies.observe(viewLifecycleOwner, {
            pagedMovieAdapter.submitList(it)
        })

        recycleViewLoadMoreScroll = RecyclerViewLoadMoreScroll(layoutManager)
        recycleViewLoadMoreScroll.setOnLoadMoreListener(object :
            RecyclerViewLoadMoreScroll.OnLoadMoreListener {
            override fun onLoadMore() {
                load()
            }

        })
        binding.movieList.addOnScrollListener(recycleViewLoadMoreScroll)

        viewModel.reachedEnd.observe(viewLifecycleOwner, {
            if (it)
                load()
        })

        return binding.root
    }

    private fun load() {
        viewModel.loadMore().observe(viewLifecycleOwner, {
            if (it)
                recycleViewLoadMoreScroll.setLoaded()
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.movieList.removeOnScrollListener(recycleViewLoadMoreScroll)
    }
}