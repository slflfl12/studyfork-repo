package io.github.slflfl12.remote.api

import io.github.slflfl12.remote.model.DiscoverMovieResponse
import io.github.slflfl12.remote.model.DiscoverTvResponse
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface DiscoverApiService {

    @GET("/3/discover/movie?language=en&sort_by=popularity.desc")
    fun getDiscoverMovie(@Query("page")page: Int): Single<DiscoverMovieResponse>

    @GET("/3/discover/tv?language=en&sort_by=popularity.desc")
    fun getDiscoverTv(@Query("page") page: Int): Single<DiscoverTvResponse>

}