package com.alorb.smarttoll.presentation.payment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.alorb.smarttoll.ui.theme.SmartTollTheme

class PaymentActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val amount  = intent.getStringExtra("amount")
            SmartTollTheme {
                PaymentActivityLayout(amount!!)
            }

        }
    }
}