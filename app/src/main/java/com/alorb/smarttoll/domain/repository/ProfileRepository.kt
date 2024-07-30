package com.alorb.smarttoll.domain.repository

import com.alorb.smarttoll.domain.Response

interface ProfileRepository {
    val displayName: String
    val photoUrl: String
    val emailID: String

    suspend fun signOut(): Response<Boolean>

    suspend fun revokeAccess(): Response<Boolean>
}