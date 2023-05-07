package com.example.game.di

import com.example.game.wheels.WheelsManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WheelsModule {

    @Singleton
    @Provides
    fun provideWheelsManager(
    ): WheelsManager {
        return WheelsManager()
    }
}