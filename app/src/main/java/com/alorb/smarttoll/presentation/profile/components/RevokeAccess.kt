package com.alorb.smarttoll.presentation.profile.components


import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.alorb.smarttoll.common.composable.ProgressBar
import com.alorb.smarttoll.domain.Response
import com.alorb.smarttoll.presentation.profile.ProfileViewModel

@Composable
fun RevokeAccess(
    viewModel: ProfileViewModel = hiltViewModel(),
    navigateToAuthScreen: (accessRevoked: Boolean) -> Unit,
    showSnackBar: () -> Unit
) {
    when(val revokeAccessResponse = viewModel.revokeAccessResponse) {
        is Response.Loading -> ProgressBar()
        is Response.Success -> revokeAccessResponse.data?.let { accessRevoked ->
            LaunchedEffect(accessRevoked) {
                navigateToAuthScreen(accessRevoked)
            }
        }
        is Response.Failure -> LaunchedEffect(Unit) {
            Log.d("RevokeAccess", "RevokeAccess: $revokeAccessResponse")
            showSnackBar()
        }
    }
}