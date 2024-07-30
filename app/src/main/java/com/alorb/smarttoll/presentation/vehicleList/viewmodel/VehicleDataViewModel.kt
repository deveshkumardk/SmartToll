package com.alorb.smarttoll.presentation.vehicleList.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alorb.smarttoll.common.VehicleData
import com.alorb.smarttoll.domain.repository.VehicleDataRepository
import com.alorb.smarttoll.presentation.toll_bill.TollBill
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VehicleDataViewModel @Inject constructor(
    private val repository: VehicleDataRepository
): ViewModel() {
    private val _vehicles = MutableStateFlow<List<VehicleData>>(emptyList())
    private var vehicles: StateFlow<List<VehicleData>> = _vehicles.asStateFlow()

    init {
        getVehicles()
    }

    fun addVehicleToFirebase(vehicleData: VehicleData) = viewModelScope.launch {
        repository.addVehicleToFirestore(vehicleData)
    }
    fun getVehicles(): StateFlow<List<VehicleData>> {
        viewModelScope.launch {
//            _vehicles.value = repository.getVehiclesFromFirestore().data!!
        try {
            val data = repository.getVehiclesFromFirestore().data ?: emptyList() // Handle null case
            _vehicles.value = data
        } catch (e: Exception) {
            // Handle exception
            e.printStackTrace()
        }
    }
        return vehicles
    }
}