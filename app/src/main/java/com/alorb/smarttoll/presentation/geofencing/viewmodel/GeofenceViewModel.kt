package com.alorb.smarttoll.presentation.geofencing.viewmodel

import androidx.lifecycle.ViewModel
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import javax.inject.Inject

class GeofenceViewModel @Inject constructor() : ViewModel() {

    @Inject
    lateinit var geofencingClient: GeofencingClient

    fun addGeofence(geofence: Geofence) {
        val geofencingRequest = GeofencingRequest.Builder().apply {
            setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            addGeofence(geofence)
        }.build()


//        geofencingClient.addGeofences(geofencingRequest, geofenceBroadcastReceiverPendingIntent)?.run {
//            addOnSuccessListener {
//                // Geofence added successfully
//            }
//            addOnFailureListener {
//                // Handle failure
//            }
//        }
    }
}