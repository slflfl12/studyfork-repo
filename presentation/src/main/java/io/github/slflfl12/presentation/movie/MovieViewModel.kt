package io.github.slflfl12.presentation.movie

import android.util.Log
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.github.slflfl12.domain.usecase.GetDiscoverMovieListUseCase
import io.github.slflfl12.presentation.base.BaseViewModel
import io.github.slflfl12.presentation.mapper.MoviePresentationMapper
import io.github.slflfl12.presentation.model.MoviePresentationModel
import io.github.slflfl12.presentation.util.Event
import io.github.slflfl12.presentation.util.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import kotlin.properties.Delegates

class MovieViewModel @ViewModelInject constructor(
    private val getDiscoverMovieListUseCase: GetDiscoverMovieListUseCase
) : BaseViewModel() {


    private val _movieList = MutableLiveData<List<MoviePresentationModel>>()
    val movieList: LiveData<List<MoviePresentationModel>>
        get() = _movieList

    private val _swipeLoading = MutableLiveData<Boolean>()
    val swipeLoading: LiveData<Boolean>
        get() = _swipeLoading

    private val _networkErrorResponse = MutableLiveData<Throwable>()
    val networkErrorResponse: LiveData<Throwable>
        get() = _networkErrorResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _toDetailEvent = MutableLiveData<Event<Pair<MoviePresentationModel, View>>>()
    val toDetailEvent: LiveData<Event<Pair<MoviePresentationModel, View>>>
        get() = _toDetailEvent

    private val _lastPagingThrowable = SingleLiveEvent<Unit>()
    val lastPagingThrowable: LiveData<Unit>
        get() = _lastPagingThrowable

    private val pageSubject: BehaviorSubject<Int> = BehaviorSubject.create()

    private val pageDisposable = CompositeDisposable()
    private val pageNum = MutableLiveData<Int>()


    init {
        onResetPage()
        pageSubject
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it == 1) {
                    refresh(it)
                } else {
                    loadMore()
                }
            }, {

            }).addTo(pageDisposable)

    }


    private fun loadMore() {
        pageNum.value?.let { pageNum ->
            getDiscoverMovieListUseCase(pageNum)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { it.map(MoviePresentationMapper::mapToPresentation) }
                .doOnSubscribe { showLoading() }
                .doAfterTerminate { hideLoading() }
                .subscribe({ movies ->
                    if (movies.isNotEmpty()) {
                        _movieList.value = movies
                    } else {
                        _lastPagingThrowable.call()
                    }
                }, {
                    Log.e("error", it.message!!)
                    _networkErrorResponse.value = it
                }).addTo(compositeDisposable)
        }

    }


    private fun refresh(pageNum: Int) {
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
                Log.e("error", it.message.toString())
                _networkErrorResponse.value = it
            }).addTo(compositeDisposable)
    }


    fun navigateToDetail(
        moviePresentationModel: MoviePresentationModel,
        ivMoviePoster: AppCompatImageView
    ) {
        _toDetailEvent.value = Event(Pair(moviePresentationModel, ivMoviePoster))
    }

    fun onResetPage() {
        pageNum.value = 1
        pageNum.value?.let {
            pageSubject.onNext(it)
        }

    }

    fun plusPage() {
        pageNum.value?.let {
            pageNum.value = it + 1
            pageSubject.onNext(it + 1)
        }
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

    fun unBindPageDisposable() {
        pageDisposable.clear()
    }

}