package com.dauto.gamediscoveryapp.di

import android.app.Application
import com.dauto.gamediscoveryapp.data.GameRepositoryImpl
import com.dauto.gamediscoveryapp.data.local.AppDatabase
import com.dauto.gamediscoveryapp.data.network.ApiFactory
import com.dauto.gamediscoveryapp.data.network.ApiService
import com.dauto.gamediscoveryapp.domain.GameRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {
    companion object {
        @ApplicationScope
        @Provides
        fun provideApiService(): ApiService {
            return ApiFactory.service
        }

        @ApplicationScope
        @Provides
        fun provideAppDatabase(
            application: Application
        ): AppDatabase {
            return AppDatabase.getInstance(application)
        }
    }

    @ApplicationScope
    @Binds
    fun bindGameRepository(gameRepositoryImpl: GameRepositoryImpl): GameRepository
}


