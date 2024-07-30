package com.alorb.smarttoll.presentation.toll_bill

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.alorb.smarttoll.R
import com.alorb.smarttoll.common.composable.MyTopAppBar
import com.alorb.smarttoll.navigation.Screen
import com.alorb.smarttoll.presentation.payment.PaymentActivity
import com.alorb.smarttoll.ui.theme.SmartTollTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TollBillActivity: ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartTollTheme {
                Scaffold(
                    topBar = {
                        MyTopAppBar(
                            title = "Toll Bill"
                        )
                    }

                ){
                    Box(
                        modifier = Modifier.padding(top =  it.calculateTopPadding())
                    ){
                        TollBillActivityLayout()
                    }
                }
            }
        }
    }
}