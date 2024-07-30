package com.alorb.smarttoll.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.alorb.smarttoll.R
import com.alorb.smarttoll.navigation.NavGraph
import com.alorb.smarttoll.navigation.Screen
import com.alorb.smarttoll.presentation.auth.AuthViewModel
import com.alorb.smarttoll.ui.theme.SmartTollTheme
import com.mappls.sdk.maps.Mappls
import com.mappls.sdk.services.account.MapplsAccountManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController
    private val viewModel by viewModels<AuthViewModel>()
    @SuppressLint("VisibleForTests")
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
//        createChannel2(this)
//        createChannel(this)
        super.onCreate(savedInstanceState)
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
            ),
            0
        )
        setContent {
            MapplsAccountManager.getInstance().restAPIKey = "ca15ec070f85a0e19f6dbb9ff5186a20"
            MapplsAccountManager.getInstance().mapSDKKey = "ca15ec070f85a0e19f6dbb9ff5186a20"
            MapplsAccountManager.getInstance().atlasClientId = "33OkryzDZsKB6P1FzSQN3CqRYhNdDf-1DhszZfDJ9aUZtuwGdqIUFoj1ygzJkTQPAzeIrfuS-u_dcpPsJ9j5oQ=="
            MapplsAccountManager.getInstance().atlasClientSecret = "lrFxI-iSEg_yB3dLY4P5tXULItHvar4QbX8wm8MhmskzqF4jUejCCfmafSzlG885yEiZ1Q9z92qxO7yqNJrNcQiHhyT0-uz8"
            Mappls.getInstance(applicationContext)
            SmartTollTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {

                        navController = rememberNavController()
                        NavGraph(
                            navController = navController,
                            applicationContext
                        )
                        navigateToProfileScreen()
                    }
                }
            }
        }
    }



    private fun checkAuthState() {
        if(viewModel.isUserAuthenticated) {
            navigateToProfileScreen()
        }
    }

    private fun navigateToProfileScreen() = navController.navigate(Screen.MainActivityLayout.route){
        popUpTo(navController.graph.id){0
            inclusive = false
        }
    }


    fun createChannel(context: Context) {
        val notificationChannel =
            NotificationChannel("CHANNEL_ID", "Channel1", NotificationManager.IMPORTANCE_HIGH)
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
            1,
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Building the notification
        val builder = NotificationCompat.Builder(context, "CHANNEL_ID")
            .setContentTitle(context.getString(R.string.app_name))
            .setContentText("hello")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(contentPendingIntent)
            .build()

        this.notify(1, builder)
    }
    fun createChannel2(context: Context) {
        val notificationChannel =
            NotificationChannel("CHANNEL_ID", "Channel1", NotificationManager.IMPORTANCE_HIGH)
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
            34,
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Building the notification
        val builder = NotificationCompat.Builder(context, "CHANNEL_ID")
            .setContentTitle(context.getString(R.string.app_name))
            .setContentText("You have exited from geofence area")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(contentPendingIntent)
            .build()

        this.notify(34, builder)
    }
}

