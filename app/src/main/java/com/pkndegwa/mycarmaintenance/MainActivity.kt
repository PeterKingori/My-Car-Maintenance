package com.pkndegwa.mycarmaintenance

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.pkndegwa.mycarmaintenance.databinding.ActivityMainBinding
import com.pkndegwa.mycarmaintenance.model.VehiclesViewModel

/**
 * Main entry point for the app.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private val vehiclesViewModel: VehiclesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set up view binding
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the navigation host fragment from this Activity
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        // Instantiate the navController using the NavHostFragment
        navController = navHostFragment.navController

        // Select nav_graph depending on whether there are saved vehicles or not
        if (vehiclesViewModel.vehiclesData.size == 0) {
            navController.setGraph(R.navigation.nav_graph)
        } else {
            navController.setGraph(R.navigation.nav_graph_2)
        }

        val appBarConfiguration = AppBarConfiguration.Builder(R.id.welcomeFragment, R.id.vehiclesFragment).build()

        // Make sure actions in the ActionBar get propagated to the NavController
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    /**
     * Enables back button support. Simply navigates one element up on the stack.
     */
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}