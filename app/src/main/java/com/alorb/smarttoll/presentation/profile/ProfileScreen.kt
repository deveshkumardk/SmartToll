package com.alorb.smarttoll.presentation.profile

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult.ActionPerformed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.alorb.smarttoll.common.constants.Constants.REVOKE_ACCESS_MESSAGE
import com.alorb.smarttoll.common.constants.Constants.SIGN_OUT
import com.alorb.smarttoll.data.repository.PreferencesRepository
import com.alorb.smarttoll.presentation.profile.components.ProfileContent
import com.alorb.smarttoll.presentation.profile.components.ProfileTopBar
import com.alorb.smarttoll.presentation.profile.components.RevokeAccess
import com.alorb.smarttoll.presentation.profile.components.SignOut
import com.alorb.smarttoll.viewmodel.PreferencesViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    prefsViewModel: PreferencesViewModel = hiltViewModel(),
    navigateToAuthScreen: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            ProfileTopBar(
                signOut = {
                    viewModel.signOut()
                },
                revokeAccess = {
                    viewModel.revokeAccess()
                }
            )
        },
        content = { padding ->
            ProfileContent(
                padding = padding,
                photoUrl = viewModel.photoUrl,
                displayName = viewModel.displayName
            )
            prefsViewModel.setPhotoUrl(context,viewModel.photoUrl)
            prefsViewModel.setEmailID(context,viewModel.emailID)
        },
//        scaffoldState = snackbarHostState
    )

    SignOut(
        navigateToAuthScreen = { signedOut ->
            if (signedOut) {
                navigateToAuthScreen()
            }
        }
    )

    fun showSnackBar() = coroutineScope.launch {

        val result = snackbarHostState.showSnackbar(
            message = REVOKE_ACCESS_MESSAGE,
            actionLabel = SIGN_OUT
        )
        if (result == ActionPerformed) {
            viewModel.signOut()
        }
    }

    RevokeAccess(
        navigateToAuthScreen = { accessRevoked ->
            if (accessRevoked) {
                navigateToAuthScreen()
            }
        },
        showSnackBar = {
            showSnackBar()
        }
    )
}