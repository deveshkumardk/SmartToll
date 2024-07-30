package com.alorb.smarttoll.navigation

import com.alorb.smarttoll.common.constants.Constants.AUTH_SCREEN
import com.alorb.smarttoll.common.constants.Constants.PROFILE_SCREEN

sealed class Screen(val route: String) {
    object AuthScreen: Screen(AUTH_SCREEN)
    object ProfileScreen: Screen(PROFILE_SCREEN)
    object MainActivityLayout: Screen("MainActivityLayout")
    object AddVehicleScreen: Screen("AddVehicleScreen")
    object VehicleListScreen: Screen("vehicleListScreen")
}
