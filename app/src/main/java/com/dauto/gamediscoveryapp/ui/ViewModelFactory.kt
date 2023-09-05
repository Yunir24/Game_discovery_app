package com.dauto.gamediscoveryapp.ui

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dauto.gamediscoveryapp.di.ApplicationScope
import javax.inject.Inject
import javax.inject.Provider


@ApplicationScope
class ViewModelFactory @Inject constructor(
    private val viewModelProviders: @JvmSuppressWildcards Map<Class<out ViewModel>, Provider<ViewModel>>
) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return viewModelProviders[modelClass]?.get() as T
    }
}
//
//class InjectingSavedStateViewModelFactory @Inject constructor(
//    private val assistedProviders: @JvmSuppressWildcards
//    Map<Class<out ViewModel>, SavedStateViewModelFactory<out ViewModel>>
//) : AbstractSavedStateViewModelFactory() {
//    override fun <T : ViewModel> create(
//        key: String,
//        modelClass: Class<T>,
//        handle: SavedStateHandle
//    ): T {
//        return assistedProviders[modelClass]?.let {
//            it.create(handle) as T
//        } ?: throw IllegalArgumentException("Unknown model class $modelClass")
//    }
//}