package com.example.game.di

import com.example.game.repository.GameRepository
import com.example.game.gameclasses.RowsManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideGameRepository(
        rowsManager: RowsManager,
    ): GameRepository {
        return GameRepository(rowsManager)
    }

}