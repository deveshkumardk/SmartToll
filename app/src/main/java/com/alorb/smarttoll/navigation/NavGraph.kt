package com.alorb.smarttoll.navigation

import android.content.Context
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.alorb.smarttoll.presentation.MainActivityLayout
import com.alorb.smarttoll.presentation.auth.AuthScreen
import com.alorb.smarttoll.presentation.profile.ProfileScreen

@Composable
@ExperimentalAnimationApi
fun NavGraph(
    navController: NavHostController,
    applicationContext: Context
) {
    NavHost(
        navController = navController,
        startDestination = Screen.AuthScreen.route,
        enterTransition = {EnterTransition.None },
        exitTransition = { ExitTransition.None }
    ) {
        composable(
            route = Screen.AuthScreen.route
        ) {
            AuthScreen(
                navigateToMainActivityLayout = {
                    navController.navigate(Screen.MainActivityLayout.route){
                        popUpTo(navController.graph.id){
                            inclusive = false
                        }
                    }
                }
            )
        }

        composable(
            route = Screen.MainActivityLayout.route
        ) {
            MainActivityLayout(
               navController = navController
            )
        }
        composable(
            route = Screen.ProfileScreen.route
        ) {
            ProfileScreen(
                navigateToAuthScreen = {
                    navController.navigate(Screen.AuthScreen.route){
                        popUpTo(navController.graph.id){
                            inclusive = false
                        }
                    }
                }
            )
        }
    }
}