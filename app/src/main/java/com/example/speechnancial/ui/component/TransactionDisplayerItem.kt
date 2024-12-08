package com.example.speechnancial.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.speechnancial.common.TransactionType
import com.example.speechnancial.data.model.Transaction
import com.example.speechnancial.data.model.TransactionDetail

@Composable
fun TransactionDisplayerItem(
    transactionResult: Transaction,
    onItemClick: () -> Unit
) {
    Column (
        Modifier.clickable { onItemClick() }
    ) {
        Text(
            if (transactionResult.type == TransactionType.UNDEFINED) "!! - ${transactionResult.type} - !!"
            else "Transaction Type : ${transactionResult.type}"
        )

        if(transactionResult.details != null) {
            transactionResult.details.forEach { transactionDetail: TransactionDetail ->
                Text("description : " + transactionDetail.description)
                Text("nominal : " + transactionDetail.nominal)
            }
        }

        Spacer(Modifier.padding(bottom = 16.dp))
    }
}