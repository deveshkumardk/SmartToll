package com.alorb.smarttoll.data.repository

import android.content.Context
import androidx.activity.ComponentActivity
import com.google.android.gms.maps.model.LatLng


class PreferencesRepository {
    companion object{
        private const val sharedPrefsName = "shared_pref"
        const val keyProfileUrl = "profile_url"
        const val keyLatStart = "latitute_start"
        const val keyLonStart = "longitute_Start"
        const val keyEmailID = "email_address"
        const val keyUserName = "user_name"

        fun setUserProfilePhoto(
            context: Context,
            photoUrl: String,
            sharedPref: String = sharedPrefsName
        ) {
            return context.getSharedPreferences(sharedPref, ComponentActivity.MODE_PRIVATE)
                .edit().putString(keyProfileUrl, photoUrl).apply()
        }

        fun getUserProfilePhoto(
            context: Context,
            sharedPref: String = sharedPrefsName
        ) : String{
            return context.getSharedPreferences(sharedPref, ComponentActivity.MODE_PRIVATE)
                .getString(keyProfileUrl,"null").toString()
        }

        fun setStartLocation(
            context: Context,
            sharedPref: String = sharedPrefsName,
            lat: String,
            lon: String
        ) {
            context.getSharedPreferences(sharedPref, ComponentActivity.MODE_PRIVATE)
                .edit().putString(keyLatStart,lat).apply()
            context.getSharedPreferences(sharedPref, ComponentActivity.MODE_PRIVATE)
                .edit().putString(keyLonStart,lon).apply()
        }
        fun getStartLocation(
            context: Context,
            sharedPref: String = sharedPrefsName
        ): LatLng {
            var lat = context.getSharedPreferences(sharedPref, ComponentActivity.MODE_PRIVATE)
                .getString(keyLatStart,"0.0").toString()
            var long = context.getSharedPreferences(sharedPref, ComponentActivity.MODE_PRIVATE)
                .getString(keyLonStart,"0.0").toString()
            return LatLng(lat.toDouble(),long.toDouble())
        }

        fun setEmailID(
            context: Context,
            sharedPref: String = sharedPrefsName,
            emailID: String
        ){
            context.getSharedPreferences(sharedPref,ComponentActivity.MODE_PRIVATE)
                .edit().putString(keyEmailID,emailID).apply()
        }

        fun getEmailID(
            context: Context,
            sharedPref: String = sharedPrefsName,
        ): String{
            val emailID = context.getSharedPreferences(sharedPref,ComponentActivity.MODE_PRIVATE)
                .getString(keyEmailID,"null").toString()
            return emailID
        }
    }
}