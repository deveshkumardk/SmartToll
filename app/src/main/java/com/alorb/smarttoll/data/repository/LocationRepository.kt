//package com.alorb.smarttoll.data.repository
//
//import android.Manifest
//import android.annotation.SuppressLint
//import android.content.Context
//import com.google.android.gms.location.FusedLocationProviderClient
//import com.google.android.gms.location.LocationRequest
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import javax.inject.Inject
//
//class LocationRepository @Inject constructor(
//    private val context: Context,
//    private val fusedLocationProviderClient: FusedLocationProviderClient
//) {
//    private val _currentLocation = MutableStateFlow<Pair<Double, Double>?>(null)
//    val currentLocation: StateFlow<Pair<Double, Double>?> = _currentLocation
//
//    @SuppressLint("MissingPermission")
//    suspend fun fetchLocation() {
//        if (hasLocationPermission()) {
//            try {
//                val location = fusedLocationProviderClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, null)
//                val lat = location.latitude
//                val lon = location.longitude
//                _currentLocation.value = Pair(lat, lon)
//            } catch (e: Exception) {
//                // Handle location request error
//            }
//        } else {
//            // Request location permission
//            requestLocationPermission()
//        }
//    }
//
//
//    // Define LOCATION_PERMISSION_REQUEST_CODE as needed
//}
//
//
//
