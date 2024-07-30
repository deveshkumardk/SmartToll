package com.alorb.smarttoll.domain.repository

import com.alorb.smarttoll.common.Resource
import com.alorb.smarttoll.common.VehicleData
import com.alorb.smarttoll.domain.Response
import com.alorb.smarttoll.presentation.toll_bill.TollBill
import kotlinx.coroutines.flow.Flow

interface VehicleDataRepository {

    suspend fun getVehiclesFromFirestore(): Resource<List<VehicleData>>

    suspend fun addVehicleToFirestore(vehicleData: VehicleData): Resource<Boolean>

    suspend fun deleteVehicleFromFirestore(vehicleNo: String): Resource<Boolean>
}