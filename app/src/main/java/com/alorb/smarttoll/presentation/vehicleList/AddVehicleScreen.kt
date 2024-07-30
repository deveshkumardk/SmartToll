package com.alorb.smarttoll.presentation.vehicleList

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.alorb.smarttoll.common.VehicleData
import com.alorb.smarttoll.presentation.vehicleList.viewmodel.VehicleDataViewModel
import com.alorb.smarttoll.viewmodel.PreferencesViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddVehicleScreen(
    vehicleDataViewModel: VehicleDataViewModel = hiltViewModel(),
    preferencesViewModel: PreferencesViewModel = hiltViewModel()
){
    val context = LocalContext.current
    var ownerName by remember {
        mutableStateOf("")
    }
    var vehicleNo by remember {

        mutableStateOf("")
    }
    var vehicleClass by remember {
        mutableStateOf("")
    }
    var emailid = preferencesViewModel.getEmailID(context).value



    Column {
        Text(
            text = "Owner Name",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
        OutlinedTextField(
            value = ownerName,
            onValueChange = { ownerName = it },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Vehicle No",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
        OutlinedTextField(
            value = vehicleNo,
            onValueChange = { vehicleNo = it },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Select Your Class",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
        OutlinedTextField(
            value = vehicleClass,
            onValueChange = { vehicleClass = it },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Email id is $emailid",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = {
            vehicleDataViewModel.addVehicleToFirebase(
                VehicleData(ownerName,vehicleNo,vehicleClass, emailid)
            )
        }){
            Text(text = "Submit")
        }
    }
}