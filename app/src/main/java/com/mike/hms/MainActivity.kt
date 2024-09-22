package com.mike.hms

import android.content.pm.PackageManager
import android.os.Bundle
import android.Manifest
import android.location.Location
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.mike.hms.ui.theme.HostelManagementSystemTheme

private const val LOCATION_PERMISSION_REQUEST_CODE = 1000

class MainActivity : AppCompatActivity() {

    // Declare fusedLocationClient at class level
    private lateinit var fusedLocationClient: com.google.android.gms.location.FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize fusedLocationClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Check for location permission and request if not granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            // Permission already granted, proceed to get location
            getLocation()
        }

        enableEdgeToEdge()

        setContent {
            HostelManagementSystemTheme(darkTheme = false, dynamicColor = false) {
                // Display location in Compose
                var locationText by remember { mutableStateOf("Fetching location...") }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(locationText)

                    // Fetch location when permission is granted
                    if (ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        getLocation { latitude, longitude ->
                            locationText = "Latitude: $latitude, Longitude: $longitude"
                        }
                    }
                }
            }
        }
    }

    // Function to get the location
    private fun getLocation(onLocationReceived: (Double, Double) -> Unit = { _, _ -> }) {
        this.fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    // Use the location object to get latitude and longitude
                    val latitude = location.latitude
                    val longitude = location.longitude
                    onLocationReceived(latitude, longitude)
                } else {
                    // Handle case when location is null (request updates if needed)
                }
            }
    }

    // Handle permission result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Permission granted, fetch location
                getLocation()
            } else {
                // Permission denied, handle accordingly
            }
        }
    }
}
