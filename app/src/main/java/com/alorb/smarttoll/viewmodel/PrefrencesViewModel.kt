package com.alorb.smarttoll.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alorb.smarttoll.data.repository.PreferencesRepository
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreferencesViewModel @Inject constructor(): ViewModel() {

    private var _profilePhotoUrl = MutableStateFlow("")
    private var profilePhotoUrl = _profilePhotoUrl.asStateFlow()

    private var _latStart = MutableStateFlow(LatLng(0.0,0.0))
    private var latStart = _latStart.asStateFlow()

    private var _emailID = MutableStateFlow("")
    private var emailID = _emailID.asStateFlow()

    fun getPhotoUrl(context: Context): StateFlow<String> {
        viewModelScope.launch {
            _profilePhotoUrl.value = PreferencesRepository.getUserProfilePhoto(context)
        }
        return profilePhotoUrl
    }

    fun setPhotoUrl(context: Context, url: String) {
        viewModelScope.launch {
            PreferencesRepository.setUserProfilePhoto(context = context, photoUrl = url)
        }
        _profilePhotoUrl.value = url
    }

    fun getStartLocation(context: Context): StateFlow<LatLng> {
        viewModelScope.launch {
            _latStart.value = PreferencesRepository.getStartLocation(context)
        }
        return latStart
    }

    fun setStartLocation(context: Context, lat: String, lon: String){
        viewModelScope.launch {
            PreferencesRepository.setStartLocation(context, lat = lat, lon = lon)
        }
        _latStart.value  = LatLng(lat.toDouble(),lon.toDouble())
    }

    fun getEmailID(
        context: Context
    ): StateFlow<String> {
        viewModelScope.launch {
            _emailID.value = PreferencesRepository.getEmailID(context)
        }
        return emailID
    }

    fun setEmailID(
        context: Context,
        emailID: String
    ){
        viewModelScope.launch {
            PreferencesRepository.setEmailID(context,emailID = emailID)
        }
        _emailID.value = emailID
    }
}