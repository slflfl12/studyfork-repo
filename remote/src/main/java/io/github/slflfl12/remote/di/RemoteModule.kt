package io.github.slflfl12.remote.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import io.github.slflfl12.data.remote.DiscoverRemoteDataSource
import io.github.slflfl12.data.remote.MovieRemoteDataSource
import io.github.slflfl12.data.remote.PeopleRemoteDataSource
import io.github.slflfl12.data.remote.TvRemoteDataSource
import io.github.slflfl12.remote.BuildConfig
import io.github.slflfl12.remote.api.DiscoverApiService
import io.github.slflfl12.remote.api.MovieApiService
import io.github.slflfl12.remote.api.PeopleApiService
import io.github.slflfl12.remote.api.TvApiService
import io.github.slflfl12.remote.interceptor.SHInterceptor
import io.github.slflfl12.remote.source.DiscoverRemoteDataSourceImpl
import io.github.slflfl12.remote.source.MovieRemoteDataSourceImpl
import io.github.slflfl12.remote.source.PeopleRemoteDataSourceImpl
import io.github.slflfl12.remote.source.TvRemoteDataSourceImpl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RemoteModule {

    private const val CONNECT_TIMEOUT = 10L
    private const val WRITE_TIMEOUT = 1L
    private const val READ_TIMEOUT = 20L

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                if(BuildConfig.DEBUG) {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            })
            .addInterceptor(SHInterceptor())
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideMovieApiService(retrofit: Retrofit): MovieApiService {
        return retrofit.create(MovieApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideMovieDataSource(movieApiService: MovieApiService): MovieRemoteDataSource {
        return MovieRemoteDataSourceImpl(movieApiService)
    }

    @Provides
    @Singleton
    fun provideTvApiService(retrofit: Retrofit): TvApiService {
        return retrofit.create(TvApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideTvDataSource(tvApiService: TvApiService): TvRemoteDataSource {
        return TvRemoteDataSourceImpl(tvApiService)
    }

    @Provides
    @Singleton
    fun provideDiscoverService(retrofit: Retrofit): DiscoverApiService {
        return retrofit.create(DiscoverApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideDiscoverDataSource(discoverApiService: DiscoverApiService): DiscoverRemoteDataSource {
        return DiscoverRemoteDataSourceImpl(discoverApiService)
    }

    @Provides
    @Singleton
    fun providePeopleService(retrofit: Retrofit): PeopleApiService {
        return retrofit.create(PeopleApiService::class.java)
    }

    @Provides
    @Singleton
    fun providePeopleDataSource(peopleApiService: PeopleApiService): PeopleRemoteDataSource {
        return PeopleRemoteDataSourceImpl(peopleApiService)
    }

}