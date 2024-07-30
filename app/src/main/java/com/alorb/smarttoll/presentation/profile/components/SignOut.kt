package com.alorb.smarttoll.presentation.profile.components


import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.alorb.smarttoll.common.composable.ProgressBar
import com.alorb.smarttoll.domain.Response
import com.alorb.smarttoll.presentation.profile.ProfileViewModel

@Composable
fun SignOut(
    viewModel: ProfileViewModel = hiltViewModel(),
    navigateToAuthScreen: (signedOut: Boolean) -> Unit
) {
    when(val signOutResponse = viewModel.signOutResponse) {
        is Response.Loading -> ProgressBar()
        is Response.Success -> signOutResponse.data?.let { signedOut ->
            LaunchedEffect(signedOut) {
                navigateToAuthScreen(signedOut)
            }
        }
        is Response.Failure -> LaunchedEffect(Unit) {
            Log.d("RevokeAccess", "RevokeAccess: $signOutResponse")
        }
    }
}