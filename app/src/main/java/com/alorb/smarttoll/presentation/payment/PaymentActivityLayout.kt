package com.alorb.smarttoll.presentation.payment

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.alorb.smarttoll.common.composable.MyTopAppBar
import com.razorpay.Checkout
import org.json.JSONArray
import org.json.JSONObject

@OptIn(ExperimentalMaterial3Api::class)
private const val RAZORPAY_PAYMENT_REQUEST_CODE = 1234

private lateinit var context: Activity
private lateinit var amount: String

class PaymentActivityLayout : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val amount = intent.getStringExtra("amount") ?: "0"
            PaymentActivityLayout(amount)
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun PaymentActivityLayout(amount: String) {
    val context = LocalContext.current as Activity
    LaunchedEffect(Unit) {
        Checkout.preload(context)
    }


    Scaffold(
        topBar = {
            MyTopAppBar(
                title = "Make Payment",
                navigationIcon = {
                    IconButton(onClick = { /* Handle back navigation here */ }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            PaymentView(context, amount)
        }
    }
}

@Composable
fun PaymentView(context: Activity, amount: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .wrapContentSize()
            .background(color = Color.White, shape = MaterialTheme.shapes.medium)
            .padding(16.dp)
    ) {
        Text(text = "Total Payment = $amount")
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = { startRazorpayPayment(context, amount) }) {
            Text(text = "Pay with Razorpay")
        }

    }
}


private fun startRazorpayPayment(context: Activity, amount: String) {
    Checkout.preload(context)
    val co = Checkout()
    co.setKeyID("rzp_test_OzKaZeSMPkEyUg") // Replace with your actual Razorpay KeyID
    try {
        val amountInRupees = amount.toDouble()
        val amountInPaise = (amountInRupees * 100).toInt()

        val options = JSONObject()
        options.put("name", "Smart Toll")
        options.put("description", "Description for the payment here")
        // You can omit the image option to fetch the image from the dashboard
        options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.jpg")
        options.put("theme.color", "#3399cc")
        options.put("currency", "INR")
        options.put("amount", amountInPaise.toString())

        // Add UPI as a payment method



        val paymentMethods = JSONArray()
        paymentMethods.put("upi")
        options.put("payment_method", paymentMethods)

        val retryObj = JSONObject()
        retryObj.put("enabled", true)
        retryObj.put("max_count", 4)
        options.put("retry", retryObj)

        val prefill = JSONObject()
        prefill.put("email", "abc@example.com")
        prefill.put("contact", "9988776655")
        options.put("prefill", prefill)
        co.open(context as Activity, options)
    } catch (e: Exception) {
        Toast.makeText(context, "Error in payment: " + e.message, Toast.LENGTH_LONG).show()
        e.printStackTrace()
    }
}





