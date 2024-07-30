package com.alorb.smarttoll.data.repository

import com.alorb.smarttoll.common.Resource
import com.alorb.smarttoll.common.VehicleData
import com.alorb.smarttoll.domain.repository.TollBillRepository
import com.alorb.smarttoll.presentation.toll_bill.TollBill
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TollBillRepositoryImp @Inject constructor( val db: FirebaseFirestore) : TollBillRepository {

    override suspend fun getTollBill(): Resource<List<TollBill>> {
        try {
            val result = db.collection("TollBill")
                .get()
                .await() // This suspends the coroutine until the result is available

            val items = result.documents.map { data->
                TollBill(
                    tollName = data["tollName"] as String,
                    tollCharge = data["tollCharge"] as String
                )
            }
            return Resource.Success(items)
        } catch (e: Exception) {
            return Resource.Error(e.message.toString())
        }
    }

    override suspend fun deleteTollBill(vehicleNo: String): Resource<Boolean> {
        TODO("Not yet implemented")
    }

}