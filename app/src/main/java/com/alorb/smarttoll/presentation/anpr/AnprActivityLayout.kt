package com.alorb.smarttoll.presentation.anpr

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alorb.smarttoll.common.composable.MyTopAppBar
import com.alorb.smarttoll.presentation.payment.PaymentActivity
import com.alorb.smarttoll.presentation.toll_bill.TollBillActivity
import com.alorb.smarttoll.presentation.vehicleList.VehicleList
import com.google.android.gms.maps.model.LatLng
import com.mappls.sdk.geojson.Point
import com.mappls.sdk.services.api.OnResponseCallback
import com.mappls.sdk.services.api.directions.DirectionsCriteria
import com.mappls.sdk.services.api.distance.MapplsDistanceMatrix
import com.mappls.sdk.services.api.distance.MapplsDistanceMatrixManager
import com.mappls.sdk.services.api.distance.models.DistanceResponse


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnprActivityLayout(){

    val context = LocalContext.current
    var latLng by remember {
        mutableStateOf<LatLng>(LatLng(0.0,0.0))
    }
    var latLng2 by remember {
        mutableStateOf<LatLng>(LatLng(0.0,0.0))
    }
    var payment by remember {
        mutableStateOf("")
    }
    var totaldistance by remember {
        mutableStateOf(0.0)
    }
    var scrollState = rememberScrollState()
    var totalkm by remember {
        mutableStateOf("dir")
    }


    payment = (totaldistance*1.45).toString()

    LaunchedEffect(Unit) {
        val distanceMatrix = MapplsDistanceMatrix.builder()
            .profile(DirectionsCriteria.PROFILE_DRIVING)
            .resource(DirectionsCriteria.RESOURCE_DISTANCE)
            .coordinate(Point.fromLngLat(latLng.longitude, latLng.latitude))
            .coordinate(Point.fromLngLat(latLng2.longitude, latLng2.latitude))
            .build()
        MapplsDistanceMatrixManager.newInstance(distanceMatrix).call(object :
            OnResponseCallback<DistanceResponse> {

            override fun onSuccess(response: DistanceResponse) {
                //handle response
//                var i:Double = 0.0
                response.also { distanceResponse ->
                    totalkm = distanceResponse.results()?.distances()!![0][0].toString()
                }
//                totalKm = i.toString()
            }

            override fun onError(code: Int, message: String) {
                //handle Error
            }
        })
    }

    totaldistance = (com.mappls.sdk.maps.geometry.LatLng(latLng.latitude, latLng.longitude)
        .distanceTo(com.mappls.sdk.maps.geometry.LatLng(latLng2.latitude, latLng2.longitude)))/1000.00


    val intent = Intent(context, TollBillActivity::class.java)

    Scaffold(
        topBar = {
            MyTopAppBar(
                title ="ANPR"
            )
        }
    ) {
        Box(
            Modifier.padding(
                top = it.calculateTopPadding(),
            )
        ) {
            Column (
                Modifier
                    .padding(10.dp)
                    .verticalScroll(state = scrollState)
            ){
                AnprActivityItem(name = "Toll Bill Screen") {
                    context.startActivity(intent)
                }
                Spacer(modifier = Modifier.height(10.dp))
                AnprActivityItem(name = "Vehile list") {
                    val intent = Intent(context, VehicleList::class.java)
                    context.startActivity(intent)
                }
                
            }
        }
    }
}

@Composable
fun AnprActivityItem(
    name: String,
    click: () -> Unit,

    ){
    Box(
        modifier = Modifier
//            .size(140.dp)
            .height(80.dp)
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFD174E0), // Light Purple
                        Color(0xFFF76497)  // Light Pink
                    ),
                    startY = 0f,
                    endY = 100f
                ),
                shape = RoundedCornerShape(5.dp)
            )
            .clickable {
                click()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = name,
            fontSize = 20.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}