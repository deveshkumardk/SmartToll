package com.alorb.smarttoll.domain.repository

import com.alorb.smarttoll.domain.Response
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.firebase.auth.AuthCredential

interface AuthRepository {
    val isUserAuthenticatedInFirebase: Boolean

    suspend fun oneTapSignInWithGoogle(): Response<BeginSignInResult>

    suspend fun firebaseSignInWithGoogle(googleCredential: AuthCredential): Response<Boolean>
}