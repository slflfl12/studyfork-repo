package io.github.slflfl12.local.di

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import io.github.slflfl12.data.local.MovieLocalDataSource
import io.github.slflfl12.local.R
import io.github.slflfl12.local.dao.MovieDao
import io.github.slflfl12.local.database.MovieDataBase
import io.github.slflfl12.local.source.MovieLocalDataSourceImpl
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object LocalModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): MovieDataBase {
        return Room.databaseBuilder(context, MovieDataBase::class.java, context.getString(R.string.database))
            .allowMainThreadQueries()
            .build()
    }

    @Provides
    @Singleton
    fun provideMovieDao(
        appDatabase: MovieDataBase
    ): MovieDao {
        return appDatabase.movieDao()
    }

    @Provides
    @Singleton
    fun provideMovieDataSource(
        movieDao: MovieDao
    ): MovieLocalDataSource {
        return MovieLocalDataSourceImpl(movieDao)
    }

}