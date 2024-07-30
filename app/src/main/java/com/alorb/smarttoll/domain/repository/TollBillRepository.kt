package com.alorb.smarttoll.domain.repository

import com.alorb.smarttoll.common.Resource
import com.alorb.smarttoll.presentation.toll_bill.TollBill

interface TollBillRepository {
    suspend fun getTollBill(): Resource<List<TollBill>>
    suspend fun deleteTollBill(vehicleNo: String): Resource<Boolean>
}