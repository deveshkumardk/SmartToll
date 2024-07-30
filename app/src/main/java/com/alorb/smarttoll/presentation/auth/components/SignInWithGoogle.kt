package com.alorb.smarttoll.presentation.auth.components

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.alorb.smarttoll.common.composable.ProgressBar
import com.alorb.smarttoll.domain.Response
import com.alorb.smarttoll.presentation.auth.AuthViewModel

@Composable
fun SignInWithGoogle(
    viewModel: AuthViewModel = hiltViewModel(),
    navigateToHomeScreen: (signedIn: Boolean) -> Unit
) {
    when(val signInWithGoogleResponse = viewModel.signInWithGoogleResponse) {
        is Response.Loading -> ProgressBar()
        is Response.Success -> signInWithGoogleResponse.data?.let { signedIn ->
            LaunchedEffect(signedIn) {
                navigateToHomeScreen(signedIn)
            }
        }
        is Response.Failure -> LaunchedEffect(Unit) {
            Log.d("SignInWithGoogle", "SignInWithGoogle: $signInWithGoogleResponse")
        }
    }
}