package com.alorb.smarttoll.presentation.vehicleList

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.alorb.smarttoll.common.VehicleData
import com.alorb.smarttoll.presentation.vehicleList.viewmodel.VehicleDataViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun VehicleListScreen(
    navigateToAddVehicleScreen: () -> Unit,
    vehicleDataViewModel: VehicleDataViewModel = hiltViewModel()
){

    val vehicleList by vehicleDataViewModel.getVehicles().collectAsState(initial = emptyList())

    val scrollState = rememberScrollState()
    Box (
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ){
//        VehicleListScreen(vehicleList = vehicleList)
        val context = LocalContext.current

//        Toast.makeText(context,vehicleList[0].vehicleNo, Toast.LENGTH_SHORT).show()

        Column(
            modifier = Modifier.verticalScroll(scrollState)
        ) {
            vehicleList.forEach {
                VehicleUnit(vehicleData = it)
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomEnd
        ) {

            ExtendedFloatingActionButton(
                onClick = {
                          navigateToAddVehicleScreen()
                },
                icon = { Icon(Icons.Filled.Edit, "") },
                text = { Text(text = "Add Vehicle Data") },
            )

        }
    }
}

@Composable
fun VehicleUnit(vehicleData: VehicleData){
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            Text(text = "Vehicle Owner - ${vehicleData.vehicleOwner}")
            Text(text = "Vehicle No - ${vehicleData.vehicleNo}")
            Text(text = "Class - ${vehicleData.vehicleClass}")
        }
    }
}
