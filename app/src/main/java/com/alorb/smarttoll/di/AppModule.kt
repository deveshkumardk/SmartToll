package com.alorb.smarttoll.di

import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.alorb.smarttoll.R
import com.alorb.smarttoll.SmartTollApplication
import com.alorb.smarttoll.common.constants.Constants.SIGN_IN_REQUEST
import com.alorb.smarttoll.common.constants.Constants.SIGN_UP_REQUEST
import com.alorb.smarttoll.data.repository.AuthRepositoryImpl
import com.alorb.smarttoll.data.repository.PreferencesRepository
import com.alorb.smarttoll.data.repository.ProfileRepositoryImpl
import com.alorb.smarttoll.data.repository.TollBillRepositoryImp
import com.alorb.smarttoll.data.repository.VehicleDataRepositoryImpl
import com.alorb.smarttoll.domain.repository.AuthRepository
import com.alorb.smarttoll.domain.repository.ProfileRepository
import com.alorb.smarttoll.domain.repository.TollBillRepository
import com.alorb.smarttoll.domain.repository.VehicleDataRepository
import com.alorb.smarttoll.presentation.geofencing.broadcast_receiver.GeofenceBroadcastReceiver
import com.alorb.smarttoll.presentation.geofencing.viewmodel.GeofenceViewModel
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
class AppModule {

    @Provides
    fun provideFirebaseAuth() = Firebase.auth

    @Provides
    fun provideFirebaseFirestore() = Firebase.firestore

    @Provides
    fun provideOneTapClient(
        @ApplicationContext
        context: Context
    ) = Identity.getSignInClient(context)

    @Provides
    @Named(SIGN_IN_REQUEST)
    fun provideSignInRequest(
        app: Application
    ) = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId(app.getString(R.string.your_web_client_id))
                .setFilterByAuthorizedAccounts(true)
                .build())
        .setAutoSelectEnabled(true)
        .build()

//    @Singleton
    @Provides
    @Named(SIGN_UP_REQUEST)
    fun provideSignUpRequest(
        app: Application
    ) = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId(app.getString(R.string.your_web_client_id))
                .setFilterByAuthorizedAccounts(false)
                .build())
        .build()

//    @Singleton
    @Provides
    fun provideGoogleSignInOptions(
        app: Application
    ) = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(app.getString(R.string.your_web_client_id))
        .requestEmail()
        .build()

//    @Singleton
    @Provides
    fun provideGoogleSignInClient(
        app: Application,
        options: GoogleSignInOptions
    ) = GoogleSignIn.getClient(app, options)

//    @Singleton
    @Provides
    fun provideAuthRepository(
        auth: FirebaseAuth,
        oneTapClient: SignInClient,
        @Named(SIGN_IN_REQUEST)
        signInRequest: BeginSignInRequest,
        @Named(SIGN_UP_REQUEST)
        signUpRequest: BeginSignInRequest,
        db: FirebaseFirestore
    ): AuthRepository = AuthRepositoryImpl(
        auth = auth,
        oneTapClient = oneTapClient,
        signInRequest = signInRequest,
        signUpRequest = signUpRequest,
        db = db
    )

//    @Singleton
    @Provides
    fun provideProfileRepository(
        auth: FirebaseAuth,
        oneTapClient: SignInClient,
        signInClient: GoogleSignInClient,
        db: FirebaseFirestore
    ): ProfileRepository = ProfileRepositoryImpl(
        auth = auth,
        oneTapClient = oneTapClient,
        signInClient = signInClient,
        db = db
    )

    @Provides
    fun provideTollBillRepository(
        db: FirebaseFirestore
    ): TollBillRepository = TollBillRepositoryImp(
        db = db
    )

    @Provides
    @Singleton
    fun providePreferencesRepository(): PreferencesRepository {
        return PreferencesRepository()
    }

    @Provides
    @Singleton
    fun provideGeofencingClient(application: SmartTollApplication): GeofencingClient {
        return LocationServices.getGeofencingClient(application)
    }

    @Provides
    @Singleton
    fun provideGeofenceViewModel(): GeofenceViewModel {
        return GeofenceViewModel()
    }

    @Provides
    @Singleton
    fun provideGeofenceBroadcastReceiverPendingIntent(
        application: SmartTollApplication
    ): PendingIntent {
        val intent = Intent(application, GeofenceBroadcastReceiver::class.java)
        return PendingIntent.getBroadcast(
            application,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }


    @Provides
    fun provideVehicleDataRepository(firestore: FirebaseFirestore
                                     , @ApplicationContext context: Context
    ): VehicleDataRepository{
        return VehicleDataRepositoryImpl(firestore,context)
    }
}