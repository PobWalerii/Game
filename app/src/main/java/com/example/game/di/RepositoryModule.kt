package com.example.game.di

import android.content.Context
import com.example.game.repository.GameRepository
import com.example.game.wheels.WheelsManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideGameRepository(
        wheelsManager: WheelsManager,
        @ApplicationContext applicationContext: Context
    ): GameRepository {
        return GameRepository(wheelsManager, applicationContext)
    }

}