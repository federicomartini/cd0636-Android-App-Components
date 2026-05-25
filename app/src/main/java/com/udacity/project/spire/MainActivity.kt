package com.udacity.project.spire

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.udacity.project.spire.databinding.ActivityMainBinding

/**
 * Main activity that hosts navigation and manages the app's UI.
 *
 * TODO #46: Implement Navigation Component setup
 *
 * This activity demonstrates:
 * 1. ViewBinding for type-safe view access
 * 2. Navigation Component for fragment navigation
 * 3. BottomNavigationView for tab navigation
 * 4. AppBarConfiguration for top-level destinations
 * 5. Dynamic UI updates based on navigation state
 *
 * KEY CONCEPTS:
 * - NavController: Manages fragment navigation
 * - AppBarConfiguration: Defines top-level destinations (no up button)
 * - BottomNavigationView: Tab bar for switching between top-level screens
 * - OnDestinationChangedListener: React to navigation events
 */
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    /**
     * Top-level destinations (show bottom nav, no up button).
     * These are the main tabs: Buildings, My Visits, Statistics.
     */
    private val topLevelDestinations = setOf(
        R.id.buildingsFragment,
        R.id.myVisitsFragment,
        R.id.statisticsFragment
    )

    private val navController: NavController by lazy {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navHostFragment.navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        appBarConfiguration = AppBarConfiguration(topLevelDestinations)
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.bottomNavigation.setupWithNavController(navController)
        setupBottomNavVisibility()
    }

    private fun setupBottomNavVisibility() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.bottomNavigation.visibility = if (destination.id in topLevelDestinations) {
                View.VISIBLE
            } else {
                View.GONE
            }

            binding.collapsingToolbar.title = when (destination.id) {
                R.id.buildingsFragment -> getString(R.string.nav_buildings)
                R.id.myVisitsFragment -> getString(R.string.nav_my_visits)
                R.id.statisticsFragment -> getString(R.string.nav_statistics)
                R.id.buildingDetailFragment -> getString(R.string.nav_building_details)
                else -> destination.label?.toString() ?: getString(R.string.app_name)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
