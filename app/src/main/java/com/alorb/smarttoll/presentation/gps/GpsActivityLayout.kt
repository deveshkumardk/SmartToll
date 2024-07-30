package com.alorb.smarttoll.presentation.gps

import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.alorb.smarttoll.common.composable.MyTopAppBar
import com.alorb.smarttoll.presentation.continuous_location.LocationService

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GpsActivityLayout(){
    val context = LocalContext.current
    Scaffold(
        topBar = {
            MyTopAppBar(
                title ="GPS"
            )
        }
    ) {
        Box(
            Modifier.padding(
                top = it.calculateTopPadding(),
            )
        ) {
            Row {
//                below code is for mobile gps
                Button(onClick = {
                    Intent(context, LocationService::class.java).apply {
                        action = LocationService.ACTION_START
                        context.startService(this)
                    }
                }) {
                    Text(text = "Start Location")
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    Intent(context, LocationService::class.java).apply {
                        action = LocationService.ACTION_STOP
                        context.startService(this)
                    }
                }) {
                    Text(text = "Stop Location")
                }
            }
        }
    }
}