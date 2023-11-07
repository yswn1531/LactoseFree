package com.sesac.lactosefree

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
            .findFragmentById(R.id.bottomNavHostFragment) as NavHostFragment? ?: return
        navController = host.navController
        binding.bottomNav.setupWithNavController(navController)
        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)
        navController.setGraph(navGraph,null)

        //네비게이션 바가 보이는 곳과 보이지 않을 곳 설정
        /*navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination == navController.findDestination(R.id.fragment_main) ||
                destination == navController.findDestination(R.id.fragment_artist) ||
                destination == navController.findDestination(R.id.fragment_schedule) ||
                destination == navController.findDestination(R.id.fragment_group) ||
                destination == navController.findDestination(R.id.fragment_setting)
            ) {
                binding.bottomNavigationView.visibility = View.VISIBLE
            } else binding.bottomNavigationView.visibility = View.GONE
        }*/
    }


}