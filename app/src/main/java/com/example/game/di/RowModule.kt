package com.example.game.di

import android.content.Context
import com.example.game.gameclasses.rows.RowsManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RowModule {

    @Singleton
    @Provides
    fun provideRowsManager(
        @ApplicationContext applicationContext: Context
    ): RowsManager {
        return RowsManager(applicationContext)
    }



}