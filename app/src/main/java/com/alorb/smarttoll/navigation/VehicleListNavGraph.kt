package com.alorb.smarttoll.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.alorb.smarttoll.presentation.vehicleList.AddVehicleScreen
import com.alorb.smarttoll.presentation.vehicleList.VehicleListScreen

@Composable
fun VehicleListNavGraph(
    navController: NavHostController
){
    NavHost(navController = navController, startDestination = Screen.VehicleListScreen.route ){
        composable(route = Screen.VehicleListScreen.route){
            VehicleListScreen(
                navigateToAddVehicleScreen = {
                    navController.navigate(Screen.AddVehicleScreen.route)
                }
            )
        }
        composable(route = Screen.AddVehicleScreen.route){
            AddVehicleScreen()
        }
    }
}