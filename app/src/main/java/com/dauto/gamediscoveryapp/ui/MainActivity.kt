package com.dauto.gamediscoveryapp.ui

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.dauto.gamediscoveryapp.R
import com.dauto.gamediscoveryapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
SavedStateViewModelFactory()
        navController = this.findNavController(R.id.nav_host_fragment_activity_main)
        navController?.let {
            navView.setupWithNavController(it)
        }
    }
    override fun onDestroy() {
        navController = null
        super.onDestroy()

    }





//    private fun setActionBar() {
//        binding.toolbar.outlineProvider = null
//        binding.appBarLayout.outlineProvider = null
//        this.setSupportActionBar(binding.toolbar)
//        this.supportActionBar.apply {
//            this?.setHomeButtonEnabled(true)
//            this?.setDisplayHomeAsUpEnabled(true)
//        }
//
//    }


}