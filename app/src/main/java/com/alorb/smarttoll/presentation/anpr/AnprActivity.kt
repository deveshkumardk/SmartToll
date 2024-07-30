package com.alorb.smarttoll.presentation.anpr

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.alorb.smarttoll.ui.theme.SmartTollTheme

class AnprActivity:ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartTollTheme {
                AnprActivityLayout()
            }
        }
    }
}