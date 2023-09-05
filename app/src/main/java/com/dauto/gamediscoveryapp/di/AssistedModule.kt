package com.dauto.gamediscoveryapp.di

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.dauto.gamediscoveryapp.ui.SavedStateViewModelFactory
import com.dauto.gamediscoveryapp.ui.home.HomeViewModel
import dagger.Binds
import dagger.Module
import dagger.Reusable
import dagger.assisted.AssistedFactory
import dagger.multibindings.IntoMap
import javax.inject.Inject

@Module
interface AssistedModule {

    @AssistedFactory
    interface Factory : SavedStateViewModelFactory<HomeViewModel> {
        override fun create(savedStateHandle: SavedStateHandle): HomeViewModel
    }

    @Binds
    @IntoMap
    @ViewModelsKey(HomeViewModel::class)
    fun bindVMFactory(factory: Factory): SavedStateViewModelFactory<out ViewModel>
}
@Reusable
class InjectingSavedStateViewModelFactory @Inject constructor(
    private val assistedProviders: @JvmSuppressWildcards
    Map<Class<out ViewModel>, SavedStateViewModelFactory<out ViewModel>>
) {
    fun create(
        onwer: SavedStateRegistryOwner,
        defaultArgs: Bundle? = null
    ): AbstractSavedStateViewModelFactory {
        return object : AbstractSavedStateViewModelFactory(onwer, defaultArgs) {
            override fun <T : ViewModel> create(
                key: String,
                modelClass: Class<T>,
                handle: SavedStateHandle
            ): T {
                assistedProviders[modelClass]?.let {
                   return it.create(handle) as T
                } ?: throw IllegalArgumentException("Unknown model class $modelClass")

            }
        }
    }
}