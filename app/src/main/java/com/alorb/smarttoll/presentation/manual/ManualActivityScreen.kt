package com.alorb.smarttoll.presentation.manual

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alorb.smarttoll.common.composable.MyTopAppBar
import com.alorb.smarttoll.presentation.continuous_location.DefaultLocationClient
import com.alorb.smarttoll.presentation.payment.PaymentActivity
import com.alorb.smarttoll.presentation.toll_bill.TollBillActivity
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.mappls.sdk.geojson.Point
import com.mappls.sdk.services.api.OnResponseCallback
import com.mappls.sdk.services.api.directions.DirectionsCriteria
import com.mappls.sdk.services.api.distance.MapplsDistanceMatrix
import com.mappls.sdk.services.api.distance.MapplsDistanceMatrixManager
import com.mappls.sdk.services.api.distance.models.DistanceResponse
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManualActivityScreen(){

    val context = LocalContext.current
    var payment by remember {
        mutableStateOf("100.00")
    }
    Scaffold(
        topBar = {
            MyTopAppBar(
                title ="Manual"
            )
        },
        bottomBar = {
            Row {
                Text(
                    text = payment,
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .align(Alignment.CenterVertically),
                    textAlign = TextAlign.Center,
                    fontSize = 30.sp
                )
                Button(
                    onClick = {
                        val intent = Intent(context, PaymentActivity::class.java)
                        intent.putExtra("amount",payment.toFloat().toString())
                        context.startActivity(intent)
                    }
                ) {
                    Text(text = "Make Payment")
                }
            }
        }

    ) {
        Box(
            Modifier.
            padding(
                top = it.calculateTopPadding(),
                bottom = it.calculateBottomPadding()
            )
        ) {
            Manual1(
                payment = {
                    payment = it
                }
            )
        }

    }
}

@Composable
fun Manual1(
    payment: (String) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    var latLng by remember {
        mutableStateOf<LatLng>(LatLng(0.0,0.0))
    }
    var latLng2 by remember {
        mutableStateOf<LatLng>(LatLng(0.0,0.0))
    }
    var selcted by remember {
        mutableStateOf(0)
    }
    var payment by remember {
        mutableStateOf("")
    }
    var totaldistance by remember {
        mutableStateOf(0.0)
    }
    payment((payment))
    var scrollState = rememberScrollState()
    var totalkm by remember {
        mutableStateOf("dir")
    }
    val locationClient = DefaultLocationClient(
        context,
        LocationServices.getFusedLocationProviderClient(context)
    )

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
                response.also { distanceResponse ->
                    totalkm = distanceResponse.results()?.distances()!![0][0].toString()
                }
            }

            override fun onError(code: Int, message: String) {

            }
        })
    }

    totaldistance = (com.mappls.sdk.maps.geometry.LatLng(latLng.latitude, latLng.longitude)
        .distanceTo(com.mappls.sdk.maps.geometry.LatLng(latLng2.latitude, latLng2.longitude)))/1000.00

    Column (
        Modifier
            .padding(10.dp)
            .verticalScroll(state = scrollState)
    ){
        
        Spacer(modifier = Modifier.height(10.dp))

        OutlinedButton(onClick = {
            locationClient
                .getLocationUpdates(10000L)
                .catch { e -> e.printStackTrace() }
                .onEach { location ->
                    val lat = location.latitude
                    val long = location.longitude
                    latLng = LatLng(lat, long)
                }
                .launchIn(coroutineScope)
        }
        ) {
            Text(text = "Get Current Location")
        }
        Spacer(modifier = Modifier.height(10.dp))
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Starting Point",
            fontSize = 25.sp,
        )
        Text(text = "(Starting Point is your current location)")

        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(5.dp))
                .clickable {

                }
                .fillMaxWidth()
                .background(color = Color.LightGray)
                .padding(5.dp)
        ){
            Text(
                text = latLng.toString(),
                fontSize = 20.sp
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Ending Point",
            fontSize = 25.sp,
        )
        Text(text = "(You can tap on below map for ending point)")
        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(5.dp))
                .clickable {
                    selcted = 1
                }
                .fillMaxWidth()
                .background(color = Color.LightGray)
                .padding(5.dp)

        ){
            Text(
                text = latLng2.toString(),
                fontSize = 20.sp
            )
        }
        Spacer(modifier = Modifier.height(10.dp))

//        Map

        GoogleMap(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 19f),
            onMapClick = {
                latLng2 = it
            },
            cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(LatLng(24.84,74.65), 5f)
            }
        ) {
            Marker(
                position = latLng,
                title = "Start",
                onClick = {
                    it.showInfoWindow()
                    true
                },
                icon = BitmapDescriptorFactory.defaultMarker(
                    BitmapDescriptorFactory.HUE_GREEN
                ),
                visible = !(latLng.longitude == 0.0 && latLng.latitude == 0.0)

            )
            Circle(
                center = LatLng(24.8445671, 74.6537666),
                radius = 2000.00,

                )
            Marker(
                position = latLng2,
                title = "End",
                onClick = {
                    it.showInfoWindow()
                    true
                },
                icon = BitmapDescriptorFactory.defaultMarker(
                    BitmapDescriptorFactory.HUE_GREEN
                ),
                visible = !(latLng2.longitude == 0.0 && latLng2.latitude == 0.0)
            )
            Polyline(
                points = listOf(latLng,latLng2),
                visible = !(latLng.longitude == 0.0 && latLng.latitude == 0.0 && latLng.longitude == 0.0 && latLng.latitude == 0.0 )
            )

        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Total Distance = ${totaldistance.toInt()} km",
            fontSize = 25.sp
        )

    }
}
