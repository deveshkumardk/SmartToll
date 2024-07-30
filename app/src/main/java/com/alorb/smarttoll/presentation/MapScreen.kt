package com.alorb.smarttoll.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.widget.Toast
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
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.akexorcist.googledirection.GoogleDirection
import com.akexorcist.googledirection.constant.AvoidType
import com.akexorcist.googledirection.util.execute
import com.alorb.smarttoll.common.composable.ClassSelection
import com.alorb.smarttoll.presentation.continuous_location.DefaultLocationClient
import com.alorb.smarttoll.presentation.continuous_location.LocationService
import com.alorb.smarttoll.presentation.toll_bill.TollBillActivity
import com.alorb.smarttoll.presentation.vehicleList.VehicleList
import com.alorb.smarttoll.qrcode.QrCodeActivity
import com.alorb.smarttoll.viewmodel.PreferencesViewModel
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

import kotlin.math.*
import com.mappls.sdk.maps.geometry.LatLng as LatLng1

@SuppressLint("SuspiciousIndentation", "StateFlowValueCalledInComposition",
    "FlowOperatorInvokedInComposition"
)
@Composable
fun MapScreen(
    payment: (String) -> Unit,
    preferencesViewModel: PreferencesViewModel = hiltViewModel()
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
    var totalkm2 by remember {
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

    totaldistance = (LatLng1(latLng.latitude,latLng.longitude).distanceTo(LatLng1(latLng2.latitude,latLng2.longitude)))/1000.00

//    totaldistance = haversine(LatLng1(latLng.latitude,latLng.longitude),LatLng1(latLng2.latitude,latLng2.longitude))


    val intent = Intent(context, TollBillActivity::class.java)

    Column (
        Modifier
            .padding(10.dp)
            .verticalScroll(state = scrollState)
    ){
        Button(onClick = {
            context.startActivity(intent)
        }) {
            Text(text = "Toll Bill Screen")
        }
        val intent = Intent(context, VehicleList::class.java)
        Button(
            onClick = {context.startActivity(intent)}
        ) {
            Text(text = "Vehile list")
        }
        OutlinedButton(onClick = {
            val intent = Intent(context, QrCodeActivity::class.java)
            context.startActivity(intent)
        }) {
            Text(text = "Qr Code Scan")
        }
        Row {
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
            text = "Select Your Class",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )

        ClassSelection()

        OutlinedButton(onClick = {
          getRoutesDistance(LatLng1(latLng.latitude,latLng.longitude),LatLng1(latLng2.latitude,latLng2.longitude), context){
              totalkm2 = it
          }
        }) {
            Text(text = "From Mappls Api")
        }


        OutlinedButton(onClick = {
            getFromGoogleApi{
                totalkm2 = it
            }
        }) {
            Text(text = "From google Api")
        }
        Text(text = totalkm2)
        Text(
            text = "Total Distance = ${totaldistance.toInt()} km",
            fontSize = 30.sp
        )
//        OutlinedTextField(
//            value = payment,
//            onValueChange = {
//                payment = it
//            },
//            placeholder = {
//                Text(text = "custom payment")
//            },
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
//        )
//        
    }
}


fun getRoutesDistance(
    latLng1: com.mappls.sdk.maps.geometry.LatLng,
    latLng2: com.mappls.sdk.maps.geometry.LatLng,
    context: Context,
    callback: (String) -> Unit
) {
    var totalKm = mutableStateOf("")
//    LaunchedEffect(Unit){
        val distanceMatrix = MapplsDistanceMatrix.builder()
            .profile(DirectionsCriteria.PROFILE_DRIVING)
            .resource(DirectionsCriteria.RESOURCE_DISTANCE)
            .coordinate(Point.fromLngLat(80.502113, 8.916787))
            .coordinate(Point.fromLngLat(28.5505073, 77.2689367))
            .build()
        MapplsDistanceMatrixManager.newInstance(distanceMatrix).call(object :
            OnResponseCallback<DistanceResponse> {

            override fun onSuccess(response: DistanceResponse) {
                //handle response
//                var i:Double = 0.0
                var string = ", "
                response.also { distanceResponse ->
//                    totalKm = distanceResponse.results()?.distances()!!.toString()
                    val double = distanceResponse.results()?.distances()!!

                    double.forEach(){array->
                        array.forEach {
                            string += ", $it"
                        }
                    }
                    callback(distanceResponse.toString())
                }
//                totalKm = i.toString()
            }
            override fun onError(code: Int, message: String) {
               Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
            }
        })

}

fun getFromGoogleApi(callback: (String) -> Unit) {
    var totalKm = ""
    GoogleDirection.withServerKey("AIzaSyAm0s0wIhW3YeAdF71NuP1_mDMU5OlZymg")
        .from(LatLng(37.7681994, -122.444538))
        .to(LatLng(37.7749003,-122.4034934))
        .avoid(AvoidType.FERRIES)
        .avoid(AvoidType.HIGHWAYS)
        .execute(
            onDirectionSuccess = {direction ->
                totalKm = if (direction!!.isOK)
                    direction?.routeList?.get(1)?.totalDistance.toString()
                else
                    direction!!.errorMessage
                callback(totalKm)
            }
        )
}

fun haversine(latLng1: com.mappls.sdk.maps.geometry.LatLng,latLng2: com.mappls.sdk.maps.geometry.LatLng): Double {
    val r = 6371 // Earth's radius in kilometers
    val lat1Rad = Math.toRadians(latLng1.latitude)
    val lon1Rad = Math.toRadians(latLng1.longitude)
    val lat2Rad = Math.toRadians(latLng2.longitude)
    val lon2Rad = Math.toRadians(latLng2.latitude)

    val dLat = lat2Rad - lat1Rad
    val dLon = lon2Rad - lon1Rad

    val a = sin(dLat / 2).pow(2) + cos(lat1Rad) * cos(lat2Rad) * sin(dLon / 2).pow(2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))

    return r * c
}

