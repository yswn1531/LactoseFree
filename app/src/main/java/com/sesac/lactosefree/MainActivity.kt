package com.sesac.lactosefree

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.sesac.lactosefree.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
        setupJetpackNavigation()
    }

    private fun setupJetpackNavigation() {
        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.bottomNavHostContainer) as NavHostFragment? ?: return
        navController = host.navController
        binding.bottomNav.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination == navController.findDestination(R.id.brandMenuFragment)  ||
                destination == navController.findDestination(R.id.favoriteDetailFragment)
            ) {
                binding.bottomNav.visibility = View.GONE
            } else binding.bottomNav.visibility = View.VISIBLE
        }
    }


}