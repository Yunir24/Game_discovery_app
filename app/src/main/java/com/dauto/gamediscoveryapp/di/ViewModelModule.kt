package com.dauto.gamediscoveryapp.di

import androidx.lifecycle.ViewModel
import com.dauto.gamediscoveryapp.ui.favorite.FavoriteViewModel
import com.dauto.gamediscoveryapp.ui.gamedetail.GameDetailViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module()
interface ViewModelModule {


//
//    @IntoMap
//    @ViewModelsKey(HomeViewModel::class)
//    @Binds
//    fun bindHomeViewModel(homeViewModel: HomeViewModel): ViewModel

    @IntoMap
    @ViewModelsKey(GameDetailViewModel::class)
    @Binds
    fun bindGameDetailViewModel(gameDetailViewModel: GameDetailViewModel): ViewModel


    @IntoMap
    @ViewModelsKey(FavoriteViewModel::class)
    @Binds
    fun bindFavoriteViewModel(favoriteViewModel: FavoriteViewModel): ViewModel

}

