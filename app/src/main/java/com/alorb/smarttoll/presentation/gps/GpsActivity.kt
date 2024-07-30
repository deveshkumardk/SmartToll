package com.alorb.smarttoll.presentation.gps

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.alorb.smarttoll.ui.theme.SmartTollTheme

class GpsActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartTollTheme {
                GpsActivityLayout()
            }
        }
    }
}