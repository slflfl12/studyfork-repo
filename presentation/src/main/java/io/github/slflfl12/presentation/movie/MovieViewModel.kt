package io.github.slflfl12.presentation.movie

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import io.github.slflfl12.domain.model.MovieModel
import io.github.slflfl12.domain.usecase.GetDiscoverMovieListUseCase
import io.github.slflfl12.domain.usecase.InsertMoviesUseCase
import io.github.slflfl12.presentation.base.BaseViewModel
import io.github.slflfl12.presentation.mapper.MoviePresentationMapper
import io.github.slflfl12.presentation.model.MoviePresentationModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import kotlin.properties.Delegates

class MovieViewModel @ViewModelInject constructor(
    private val getDiscoverMovieListUseCase: GetDiscoverMovieListUseCase,
    private val insertMoviesUseCase: InsertMoviesUseCase
) : BaseViewModel() {


    private val _movieList = MutableLiveData<List<MoviePresentationModel>>()
    val movieList: LiveData<List<MoviePresentationModel>>
        get() = _movieList

    private val _swipeLoading = MutableLiveData<Boolean>()
    val swipeLoading: LiveData<Boolean>
            get() = _swipeLoading

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading


    private var pageNum by Delegates.notNull<Int>()

    private val pageLiveData: MutableLiveData<Int> = MutableLiveData()


    init {
        refresh()
    }


    fun loadMore() {
        getDiscoverMovieListUseCase(++pageNum)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { it.map(MoviePresentationMapper::mapToPresentation) }
            .doOnSubscribe { showLoading() }
            .doAfterTerminate { hideLoading() }
            .subscribe({ movies ->
                _movieList.value = movies
            }, {
                Log.d("error", it.message!!)
            }).addTo(compositeDisposable)
    }


    fun refresh() {
        setMoviePage(1)
        getDiscoverMovieListUseCase(pageNum)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showSwipeLoading() }
            .doAfterTerminate { hideSwipeLoading() }
            .map { it.map(MoviePresentationMapper::mapToPresentation) }
            .subscribe({ movies ->
                if (movies.isNotEmpty()) {
                    _movieList.value = movies
                }
            }, {
                Log.d("error", it.message.toString())
            }).addTo(compositeDisposable)
    }

    private fun setMoviePage(pageNum: Int) {
        this.pageNum = pageNum
    }

    private fun showLoading() {
        _isLoading.value = true
    }

    private fun hideLoading() {
        _isLoading.value = false
    }

    private fun showSwipeLoading() {
        _swipeLoading.value = true
    }

    private fun hideSwipeLoading() {
        _swipeLoading.value = false
    }

}