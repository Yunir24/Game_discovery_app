package com.dauto.gamediscoveryapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dauto.gamediscoveryapp.di.InjectingSavedStateViewModelFactory
import com.dauto.gamediscoveryapp.ui.home.HomeViewModel
import com.dauto.gamediscoveryapp.ui.utils.getAppComponent
import javax.inject.Inject

class testFrament : Fragment(){
    private val component by lazy {
        getAppComponent()
    }
    @Inject
    lateinit var abst: InjectingSavedStateViewModelFactory

    private val factory by lazy {
        abst.create(onwer = requireActivity(), null)
    }
    private val homeViewModel by lazy {
        ViewModelProvider(requireActivity(), factory)[HomeViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.
    }
}