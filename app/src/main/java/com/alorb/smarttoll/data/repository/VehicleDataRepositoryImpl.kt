package com.alorb.smarttoll.data.repository

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import com.alorb.smarttoll.common.Resource
import com.alorb.smarttoll.common.VehicleData
import com.alorb.smarttoll.common.constants.Constants.USERS
import com.alorb.smarttoll.common.constants.Constants.VEHICLEDATA
import com.alorb.smarttoll.domain.Response
import com.alorb.smarttoll.domain.repository.VehicleDataRepository
import com.alorb.smarttoll.viewmodel.PreferencesViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VehicleDataRepositoryImpl @Inject constructor (val db: FirebaseFirestore, val context: Context):VehicleDataRepository {
    override suspend fun getVehiclesFromFirestore(): Resource<List<VehicleData>> {
        try {
            val email = PreferencesRepository.getEmailID(context,"shared_pref")
            val result = db.collection("VehicleData")
                .whereEqualTo("emailId", email)
                .get()
                .await() // This suspends the coroutine until the result is available

            val items = result.documents.map { data->
                VehicleData(
                    vehicleOwner = data["vehicleOwner"] as String,
                    vehicleNo = data["vehicleNo"] as String,
                    emailId = data["emailId"] as String,
                    vehicleClass = data["vehicleClass"] as String
                )
            }
            return Resource.Success(items)
        } catch (e: Exception) {
            return Resource.Error(e.message.toString())
        }
    }

    override suspend fun addVehicleToFirestore(vehicleData: VehicleData): Resource<Boolean> {
        val response = try {
//            val id = db.collection("VehicleData").id
            db.collection("VehicleData").document().set(vehicleData).await()
        } catch (e: Exception){
            return Resource.Error(e.message.toString())
        }
        return Resource.Success(true)
    }

    override suspend fun deleteVehicleFromFirestore(vehicleNo: String): Resource<Boolean> {
        TODO("Not yet implemented")
    }
}