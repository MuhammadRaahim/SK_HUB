package com.jdars.shared_online_business.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.jdars.shared_online_business.CallBacks.DrawerHandler
import com.jdars.shared_online_business.CallBacks.GenericHandler
import com.jdars.shared_online_business.R
import com.jdars.shared_online_business.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity(), DrawerHandler, GenericHandler {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var bottomNavView: BottomNavigationView
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpUi()
        setUpBottomNavigation()

    }

    private fun setUpNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_home) as NavHostFragment
        navController = navHostFragment.navController
    }

    private fun setUpUi() {
        bottomNavView = binding.bottomNavView
        setUpNavigation()
    }

    private fun setUpBottomNavigation() {
        bottomNavView.setupWithNavController(navController)
    }

    override fun openDrawer() {
        binding.drawerLayout.openDrawer(GravityCompat.START)
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

    override fun showNoInternet(show: Boolean) {
    }
}