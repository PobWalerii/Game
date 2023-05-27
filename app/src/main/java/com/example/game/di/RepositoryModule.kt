package com.example.game.di

import com.example.game.repository.GameRepository
import com.example.game.gameclasses.rows.RowsManager
import com.example.game.gameclasses.wheels.WheelsManager
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
        wheelsManager: WheelsManager,
        rowsManager: RowsManager,
    ): GameRepository {
        return GameRepository(wheelsManager, rowsManager)
    }

}