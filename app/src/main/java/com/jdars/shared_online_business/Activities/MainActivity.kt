package com.jdars.shared_online_business.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.jdars.shared_online_business.CallBacks.DrawerHandler
import com.jdars.shared_online_business.CallBacks.GenericHandler
import com.jdars.shared_online_business.R
import com.jdars.shared_online_business.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), DrawerHandler, GenericHandler {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavView: BottomNavigationView
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpUi()
        setUpBottomNavigation()
    }

    private fun setUpUi() {
       bottomNavView = binding.bottomNavView
       navController = findNavController(R.id.home_nav_host_fragment)
    }

    private fun setUpBottomNavigation() {
        bottomNavView.setupWithNavController(navController)
    }

    override fun openDrawer() {
        binding.drawer.openDrawer(GravityCompat.START)
    }

    override fun showProgressBar(show: Boolean) {
        binding.progressLayout.isVisible = show
    }

    override fun showMessage(message: String) {
        Snackbar.make(
            findViewById(android.R.id.content),
            message, Snackbar.LENGTH_LONG
        ).show()
    }

    override fun showNoInternet(show: Boolean) {}
}