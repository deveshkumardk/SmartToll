package com.alorb.smarttoll.presentation.vehicleList

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.alorb.smarttoll.common.VehicleData
import com.alorb.smarttoll.navigation.VehicleListNavGraph
import com.alorb.smarttoll.ui.theme.SmartTollTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VehicleList: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartTollTheme {
                val navHostController = rememberNavController()
                val vehicleList:List<VehicleData> = listOf(
                    VehicleData("name1","VEhicle NO", "Vehicle class",""),
                    VehicleData("name2","VEhicle NO", "Vehicle class",""),
                    VehicleData("name3","VEhicle NO", "Vehicle class",""),
                    VehicleData("name4","VEhicle NO", "Vehicle class","")
                )
                VehicleListNavGraph(navController = navHostController)

            }

        }
    }
}