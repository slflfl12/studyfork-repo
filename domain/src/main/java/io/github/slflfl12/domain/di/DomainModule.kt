package io.github.slflfl12.domain.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import io.github.slflfl12.domain.repository.DiscoverRepository
import io.github.slflfl12.domain.repository.MovieRepository
import io.github.slflfl12.domain.repository.PeopleRepository
import io.github.slflfl12.domain.repository.TvRepository
import io.github.slflfl12.domain.usecase.*
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object DomainModule {

    @Provides
    @Singleton
    fun provideGetDiscoverMovieListUseCase(
        movieRepository: MovieRepository,
        discoverRepository: DiscoverRepository
    ): GetDiscoverMovieListUseCase {
        return GetDiscoverMovieListUseCase(movieRepository, discoverRepository)
    }


    @Provides
    @Singleton
    fun provideGetMovieKeywordListUseCase(
        movieRepository: MovieRepository
    ): GetMovieKeywordListUseCase {
        return GetMovieKeywordListUseCase(movieRepository)
    }

    @Provides
    @Singleton
    fun provideGetMovieReviewListUseCase(
        movieRepository: MovieRepository
    ): GetMovieReviewListUseCase {
        return GetMovieReviewListUseCase(movieRepository)
    }

    @Provides
    @Singleton
    fun provideGetMovieVideoListUseCase(
        movieRepository: MovieRepository
    ): GetMovieVideoListUseCase {
        return GetMovieVideoListUseCase(movieRepository)
    }

    @Provides
    @Singleton
    fun getLocalMovieUseCase(
        movieRepository: MovieRepository
    ): GetLocalMovieUseCase {
        return GetLocalMovieUseCase(movieRepository)
    }

    @Provides
    @Singleton
    fun getTvListUseCase(
        tvRepository: TvRepository,
        discoverRepository: DiscoverRepository
    ): GetDiscoverTvListUseCase {
        return GetDiscoverTvListUseCase(tvRepository, discoverRepository)
    }

    @Provides
    @Singleton
    fun getTvKeywordListUseCase(
        tvRepository: TvRepository
    ): GetTvKeywordListUseCase {
        return GetTvKeywordListUseCase(tvRepository)
    }

    @Provides
    @Singleton
    fun getTvReviewListUseCase(
        tvRepository: TvRepository
    ): GetTvReviewListUseCase {
        return GetTvReviewListUseCase(tvRepository)
    }

    @Provides
    @Singleton
    fun getTvVideoListUseCase(
        tvRepository: TvRepository
    ): GetTvVideoListUseCase {
        return GetTvVideoListUseCase(tvRepository)
    }

    @Provides
    @Singleton
    fun getLocalTvUseCase(
        tvRepository: TvRepository
    ): GetLocalTvUseCase {
        return GetLocalTvUseCase(tvRepository)
    }

    @Provides
    @Singleton
    fun getPeopleListUseCase(
        peopleRepository: PeopleRepository
    ): GetPeopleListUseCase {
        return GetPeopleListUseCase(peopleRepository)
    }

    @Provides
    @Singleton
    fun getPersonWithDetailUseCase(
        peopleRepository: PeopleRepository
    ): GetPersonWithDetailUseCase {
        return GetPersonWithDetailUseCase(peopleRepository)
    }

    @Provides
    @Singleton
    fun getPersonMovieUseCase(
        peopleRepository: PeopleRepository
    ): GetPersonMovieUseCase {
        return GetPersonMovieUseCase(peopleRepository)
    }

    @Provides
    @Singleton
    fun getPersonTvUseCase(
        peopleRepository: PeopleRepository
    ): GetPersonTvUseCase {
        return GetPersonTvUseCase(peopleRepository)
    }

    @Provides
    @Singleton
    fun getMovieFavoriteUseCase(
        movieRepository: MovieRepository
    ): GetMovieFavoriteUseCase {
        return GetMovieFavoriteUseCase(movieRepository)
    }

    @Provides
    @Singleton
    fun getTvFavoriteUseCase(
        tvRepository: TvRepository
    ): GetTvFavoriteUseCase {
        return GetTvFavoriteUseCase(tvRepository)
    }

    @Provides
    @Singleton
    fun getFavoriteMovieListUseCase(
        movieRepository: MovieRepository
    ): GetFavoriteMovieListUseCase {
        return GetFavoriteMovieListUseCase(movieRepository)
    }

    @Provides
    @Singleton
    fun getFavoriteTvListUseCase(
        tvRepository: TvRepository
    ): GetFavoriteTvListUseCase {
        return GetFavoriteTvListUseCase(tvRepository)
    }

}