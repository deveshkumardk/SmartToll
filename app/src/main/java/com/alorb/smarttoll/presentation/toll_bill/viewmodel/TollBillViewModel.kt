package com.alorb.smarttoll.presentation.toll_bill.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alorb.smarttoll.domain.repository.TollBillRepository
import com.alorb.smarttoll.presentation.toll_bill.TollBill
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TollBillViewModel @Inject constructor(
    private val repository: TollBillRepository
): ViewModel() {
    private val _tollBill = MutableStateFlow<List<TollBill>>(emptyList())
    val tollBill: StateFlow<List<TollBill>> = _tollBill.asStateFlow()

    init {
        fetchTollBills()
    }

    private fun fetchTollBills() {
        viewModelScope.launch {
            try {
                val fetchedTollBills = repository.getTollBill().data!!
                _tollBill.emit(fetchedTollBills)
            } catch (e: Exception) {
                // Handle error appropriately
            }
        }
    }
}