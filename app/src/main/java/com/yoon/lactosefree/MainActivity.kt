package com.yoon.lactosefree

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.yoon.lactosefree.brand.BrandViewModel
import com.yoon.lactosefree.common.PermissionCheck
import com.yoon.lactosefree.common.PermissionManager
import com.yoon.lactosefree.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var permissionCheck: PermissionCheck

    private val viewModel: BrandViewModel by lazy {
        ViewModelProvider(this).get(BrandViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
        if (!PermissionManager.getInstance().isPermission) {
            permissionCheck()
        }
        viewModel.getBrandImageFromStorage()
        viewModel.getBrandBeverageImageFromStorage()
        setupJetpackNavigation()
    }

    private fun setupJetpackNavigation() {
        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.bottomNavHostContainer) as NavHostFragment? ?: return
        navController = host.navController
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination == navController.findDestination(R.id.brandMenuFragment)  ||
                destination == navController.findDestination(R.id.favoriteDetailFragment)
            ) {
                binding.bottomNav.visibility = View.GONE
            } else binding.bottomNav.visibility = View.VISIBLE
        }
        binding.bottomNav.setupWithNavController(navController)
    }

    private fun permissionCheck() {
        permissionCheck = PermissionCheck(applicationContext, this)
        if (!permissionCheck.currentAppCheckPermission()) {
            permissionCheck.currentAppRequestPermissions()
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (!permissionCheck.currentAppPermissionResult(requestCode,grantResults)) {
            permissionCheck.currentAppRequestPermissions()
        }else{
            PermissionManager.getInstance().isPermission = true
        }
    }


}