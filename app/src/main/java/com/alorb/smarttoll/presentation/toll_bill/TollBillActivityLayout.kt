package com.alorb.smarttoll.presentation.toll_bill

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.alorb.smarttoll.presentation.payment.PaymentActivity
import com.alorb.smarttoll.presentation.toll_bill.viewmodel.TollBillViewModel

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun TollBillActivityLayout( viewModel: TollBillViewModel = hiltViewModel()){

//    val tollBills = viewModel.tollBill.value

    val tollBills by viewModel.tollBill.collectAsState(initial = emptyList())
    val totalCharge = remember(tollBills) {
        var charge = 0
        tollBills.forEach { tollBill ->
            charge += tollBill.tollCharge.toInt()
        }
        charge
    }
    val context = LocalContext.current

    Column {
        tollBills.forEach{tollBill->
            TollBillItem(
                tollName = tollBill.tollName,
                tollCharge = tollBill.tollCharge
            )
            Spacer(Modifier.height(10.dp))
        }
        Box(modifier = Modifier.fillMaxWidth()){
            Text(text = "Total Charge = $totalCharge")
        }
        Button(onClick = {
            val intent = Intent(context,PaymentActivity::class.java)
            intent.putExtra("amount",totalCharge.toFloat().toString())
            context.startActivity(intent)
        }) {
            Text(text = "PAY BILL")
        }
    }
}

@Composable
fun TollBillItem(tollName: String, tollCharge: String){
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ){
        Column {
            Text(text = tollName)
            Row {
                Text(text = "Charge :")
                Spacer(modifier = Modifier.width(30.dp))
                Text(text = tollCharge)

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TollBillItemPreview(){
    TollBillItem("Toll Name", "100")
}

@Preview(showBackground = true)
@Composable
fun TollBillLayoutPreview(){

}