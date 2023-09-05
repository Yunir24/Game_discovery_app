package com.dauto.gamediscoveryapp.di

import android.app.Application
import com.dauto.gamediscoveryapp.ui.favorite.FavoriteFragment
import com.dauto.gamediscoveryapp.ui.gamedetail.GameDetailFragment
import com.dauto.gamediscoveryapp.ui.home.FilterFragment
import com.dauto.gamediscoveryapp.ui.home.HomeFragment
import dagger.BindsInstance
import dagger.Component


@ApplicationScope
@Component(
    modules = [
        AssistedModule::class,
        DataModule::class,
        ViewModelModule::class
    ]
)
interface ApplicationComponent {


    fun inject(homeFragment: HomeFragment)
    fun inject(gameDetailFragment: GameDetailFragment)
    fun inject(favoriteFragment: FavoriteFragment)
    fun inject(filterFragment: FilterFragment)


    @Component.Factory
    interface ApplicationComponentFactory{

        fun create(
            @BindsInstance  application: Application
        ): ApplicationComponent
    }
}