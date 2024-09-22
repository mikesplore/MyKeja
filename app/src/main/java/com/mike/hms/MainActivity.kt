package com.mike.hms

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.mike.hms.ui.theme.HostelManagementSystemTheme

class MainActivity : ComponentActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    // Define the permission launcher
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            getLocation()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        HMSPreferences.initialize(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        enableEdgeToEdge()

        // Check and request location permission
        if (LocationUtils.isLocationPermissionGranted(this)) {
            getLocation()
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }

        setContent {
            HostelManagementSystemTheme(darkTheme = HMSPreferences.darkMode.value, dynamicColor = false) {
                val window = (LocalView.current.context as Activity).window
                WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = !HMSPreferences.darkMode.value
                // Your content goes here
                NavGraph(this)
            }
        }
    }

    private fun getLocation() {
        LocationUtils.getLocation(this, fusedLocationClient) { cityName ->
            HMSPreferences.saveLocation(cityName ?: "Unable to fetch location")
        }
    }
}


