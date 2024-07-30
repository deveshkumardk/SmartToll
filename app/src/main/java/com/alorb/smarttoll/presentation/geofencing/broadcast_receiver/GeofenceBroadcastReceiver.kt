package com.alorb.smarttoll.presentation.geofencing.broadcast_receiver

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.alorb.smarttoll.R
import com.alorb.smarttoll.presentation.MainActivity
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent

//@AndroidEntryPoint
class GeofenceBroadcastReceiver () : BroadcastReceiver() {
    private val NOTIFICATION_ID = 33
    private val CHANNEL_ID = "GeofenceChannel"

    @SuppressLint("LogNotTimber", "VisibleForTests")
    override fun onReceive(context: Context, intent: Intent) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent.hasError()) {
            Log.e("GeofenceReceiver", "GeofencingEvent error: ${geofencingEvent.errorCode}")
            return
        }
        val geofenceTransition = geofencingEvent.geofenceTransition
        val transitionType = geofencingEvent.geofenceTransition
        if (transitionType == Geofence.GEOFENCE_TRANSITION_ENTER) {
            // Handle geofence enter transition
            val triggeringGeofences = geofencingEvent.triggeringGeofences
            val triggeredGeofence = triggeringGeofences[0]

            Log.d("GeofenceReceiver", "Entered geofence")

            geofencingEvent.triggeringGeofences.firstOrNull()?.let { triggeringGeofence ->
                val location = geofencingEvent.getTriggeringLocation()
                val latitude = location.latitude
                val longitude = location.longitude
                // Now you have the middle coordinates (x2, y2) of the geofence
                // Use latitude and longitude as needed
            }

            Toast.makeText(context, "You are entering in geofence area! ", Toast.LENGTH_SHORT).show()
            createChannel(context)

        }
        if (transitionType == Geofence.GEOFENCE_TRANSITION_DWELL) {
            // Handle geofence enter transition
            Log.d("GeofenceReceiver", "Entered geofence")

            // Display a toast
            Toast.makeText(context, "You are in the geofence area!", Toast.LENGTH_SHORT).show()
            createChannel(context)
        }
        if (transitionType == Geofence.GEOFENCE_TRANSITION_EXIT) {
            // Handle geofence enter transition
            Log.d("GeofenceReceiver", "Entered geofence")

            // Display a toast
            createChannel2(context)

        }

    }
    fun createChannel(context: Context) {
        val notificationChannel =
            NotificationChannel(CHANNEL_ID, "Channel1", NotificationManager.IMPORTANCE_HIGH)
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(notificationChannel)
        notificationManager.sendGeofenceEnteredNotification(context)
    }

    // extension function
    fun NotificationManager.sendGeofenceEnteredNotification(context: Context) {

        // Opening the notification
        val contentIntent = Intent(context, MainActivity::class.java)
        val contentPendingIntent = PendingIntent.getActivity(
            context,
            NOTIFICATION_ID,
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Building the notification
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(context.getString(R.string.app_name))
            .setContentText("You have entered a geofence area")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(contentPendingIntent)
            .build()

        this.notify(NOTIFICATION_ID, builder)
    }
    fun createChannel2(context: Context) {
        val notificationChannel =
            NotificationChannel(CHANNEL_ID, "Channel1", NotificationManager.IMPORTANCE_HIGH)
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(notificationChannel)
        notificationManager.sendGeofenceExitNotification(context)
    }

    // extension function
    fun NotificationManager.sendGeofenceExitNotification(context: Context) {

        // Opening the notification
        val contentIntent = Intent(context, MainActivity::class.java)
        val contentPendingIntent = PendingIntent.getActivity(
            context,
            NOTIFICATION_ID,
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Building the notification
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(context.getString(R.string.app_name))
            .setContentText("You have exited from geofence area")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(contentPendingIntent)
            .build()

        this.notify(NOTIFICATION_ID, builder)
    }
}
