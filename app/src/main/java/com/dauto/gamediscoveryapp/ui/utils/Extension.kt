package com.dauto.gamediscoveryapp.ui.utils

import android.view.View
import androidx.fragment.app.Fragment
import com.dauto.gamediscoveryapp.GameDiscoveryApp
import com.dauto.gamediscoveryapp.di.ApplicationComponent


fun Fragment.getAppComponent(): ApplicationComponent =
    (requireActivity().application as GameDiscoveryApp).component

fun View.setInvisible() {
    this.visibility = View.INVISIBLE
}
fun View.setGone() {
    this.visibility = View.GONE
}

fun View.setVisible() {
    this.visibility = View.VISIBLE
}
