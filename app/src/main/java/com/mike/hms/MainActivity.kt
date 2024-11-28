package com.mike.hms

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.mike.hms.model.paymentMethods.CreditCardViewModel
import com.mike.hms.model.favorites.FavoriteViewModel
import com.mike.hms.model.houseModel.HouseViewModel
import com.mike.hms.model.userModel.UserViewModel
import com.mike.hms.ui.theme.HostelManagementSystemTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.runtime.getValue
import com.mike.hms.profile.WalletScreen
import kotlinx.coroutines.delay

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
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
            HostelManagementSystemTheme(
                darkTheme = HMSPreferences.darkMode.value,
                dynamicColor = false
            ) {
                val houseViewModel: HouseViewModel = hiltViewModel()
                val userViewModel: UserViewModel = hiltViewModel()
                val houses = houseViewModel.houses.collectAsState()
                val favoriteViewModel: FavoriteViewModel = hiltViewModel()
                val creditCardViewModel: CreditCardViewModel = hiltViewModel()
                val window = (LocalView.current.context as Activity).window
                val user by userViewModel.user.collectAsState()
                val email = FirebaseAuth.getInstance().currentUser?.email
                WindowCompat.getInsetsController(
                    window,
                    window.decorView
                ).isAppearanceLightStatusBars = !HMSPreferences.darkMode.value

                LaunchedEffect(email) {
                    val maxRetries = 3
                    var attempt = 0

                    while (attempt < maxRetries) {
                        userViewModel.getUserByEmail(email.toString())
                        Log.d("UserViewModell", "Attempt ${attempt + 1}: Searching user of email: $email")

                        if (user != null) {
                            Log.d("UserViewModell", "User found: ${user?.userID}")
                            HMSPreferences.saveUserId(user!!.userID)
                            break
                        }

                        attempt++
                        delay(1000L) // Suspend the coroutine for 1 second instead of blocking
                    }

                    if (user == null) {
                        Log.d("UserViewModell", "User not found after $maxRetries attempts.")
                    }
                }


                // Your content goes here

              NavGraph(houseViewModel, houses.value, favoriteViewModel, userViewModel, creditCardViewModel, this)
            }
        }
    }

    private fun getLocation() {
        LocationUtils.getLocation(this, fusedLocationClient) { cityName ->
            HMSPreferences.saveLocation(cityName ?: "Unable to fetch location")
        }
    }
}


