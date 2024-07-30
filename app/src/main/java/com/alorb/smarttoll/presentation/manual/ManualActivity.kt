package com.alorb.smarttoll.presentation.manual

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.alorb.smarttoll.ui.theme.SmartTollTheme

class ManualActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartTollTheme {
                ManualActivityScreen()
            }
        }
    }
}