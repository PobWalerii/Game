package com.example.game.di

import android.content.Context
import com.example.game.gamesclasses.wheels.WheelsManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WheelsModule {

    @Singleton
    @Provides
    fun provideWheelsManager(
        @ApplicationContext applicationContext: Context
    ): WheelsManager {
        return WheelsManager(applicationContext)
    }
}