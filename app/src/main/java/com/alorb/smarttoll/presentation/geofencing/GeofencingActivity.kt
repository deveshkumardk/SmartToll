package com.alorb.smarttoll.presentation.geofencing
import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import com.alorb.smarttoll.presentation.geofencing.broadcast_receiver.GeofenceBroadcastReceiver
import com.alorb.smarttoll.ui.theme.SmartTollTheme
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GeofencingActivity : ComponentActivity(), OnMapReadyCallback {

    private lateinit var geofencingClient: GeofencingClient
    private var geofenceList = ArrayList<Geofence>()
    private val geofenceIntent: PendingIntent by lazy {
        val intent = Intent(this, GeofenceBroadcastReceiver::class.java)
        PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private lateinit var mapView: MapView

    @SuppressLint("VisibleForTests")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        geofencingClient = LocationServices.getGeofencingClient(this)

        mapView = MapView(this)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        setContent {
            SmartTollTheme {
                GeofencingActivityLayout()
            }
        }

        addGeofences()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        googleMap ?: return

        val builder = LatLngBounds.Builder()
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION

            ) == PackageManager.PERMISSION_GRANTED
        ) {
            googleMap.isMyLocationEnabled = true
            googleMap.uiSettings.isMyLocationButtonEnabled = true
            val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val currentLatLng = LatLng(location.latitude, location.longitude)

                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
                }
            }
        }
        // Add markers and circles for each geofence location
        geofenceList.forEachIndexed { index, geofence ->
            val geofenceLocation = LatLng(
                geofence.requestId.split("_")[1].toDouble(),
                geofence.requestId.split("_")[2].toDouble()
            )
            val geofenceRadius = geofence.requestId.split("_")[3].toFloat()

            googleMap.addMarker(
                MarkerOptions()
                    .position(geofenceLocation)
                    .title("Geofence $index")
            )

            // Add circle representing the geofence with fill color
            googleMap.addCircle(
                CircleOptions()
                    .center(geofenceLocation)
                    .radius(geofenceRadius.toDouble())
                    .fillColor(Color.argb(70, 0, 255, 0)) // Green fill color with 70% opacity
                    .strokeColor(Color.BLUE) // Stroke color
            )
            builder.include(geofenceLocation)
        }
        val bounds = builder.build()
        val padding = 100 // Padding around the bounds
        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding)
        googleMap.moveCamera(cameraUpdate)
    }

    private fun addGeofences() {
        val geofenceData = listOf(
            Triple(26.858893,75.757859 , 500f),
            Triple(12.9604269,77.6391294 , 500f),  // First geofence data
            Triple(12.9662132, 77.7167558, 500f),               // Second geofence data
            Triple(12.9569185, 77.7008464, 500f)                // Third geofence data
        )

        geofenceData.forEachIndexed { index, (latitude, longitude, radius) ->
            geofenceList.add(
                Geofence.Builder()
                    .setRequestId("geofence_${latitude}_${longitude}_${radius}")
                    .setCircularRegion(latitude, longitude, radius)
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
                    .build()
            )
        }

        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        geofencingClient.addGeofences(getGeofencingRequest(), geofenceIntent)?.run {
            addOnSuccessListener {
                Toast.makeText(this@GeofencingActivity, "Geofences added", Toast.LENGTH_SHORT).show()
            }
            addOnFailureListener {
                Toast.makeText(this@GeofencingActivity, "Failed to add geofences", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getGeofencingRequest(): GeofencingRequest {
        return GeofencingRequest.Builder().apply {
            setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            addGeofences(geofenceList)
        }.build()
    }

    @Composable
    fun GeofencingActivityLayout() {
        MapViewContainer(mapView)
    }

    @Composable
    fun MapViewContainer(mapView: MapView) {
        MapView(mapView)
    }

    @Composable
    fun MapView(
        mapView: MapView,
        modifier: Modifier = Modifier
    ) {
        AndroidView({ mapView }, modifier = modifier) { mapView ->
            mapView.getMapAsync { }
        }
    }
}
