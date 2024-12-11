package com.example.speechnancial.ui.component.inputTransactionScreen

import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.speechnancial.common.TransactionType
import com.example.speechnancial.data.model.Transaction
import com.example.speechnancial.ui.preview.TransactionItemPreviewParameterProvider
import com.example.speechnancial.ui.theme.SpeechnancialTheme
import com.example.speechnancial.ui.theme.transpernt
import com.example.speechnancial.viewmodel.transactionListScreen.TransactionItemState
import java.time.format.DateTimeFormatter

@Composable
fun ProposedTransaction(
    transaction: Transaction,
    isExpanded: Boolean,
    onItemClick: () -> Unit,
    onDropdownClick: () -> Unit,
) {
    Column (
        Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                )
            )
            .padding(16.dp)
            .clickable { onItemClick() },
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // avoiding gap content
        Column {
            transaction.details?.forEach {
                Row {
                    Text(
                        it.description,
                        modifier = Modifier.weight(1f)
                    )
                    Text("Rp. ${it.nominal}")
                }
            }
        }

        HorizontalDivider(
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .fillMaxWidth()  //fill the max height
                .width(1.dp)
        )

        Text(
            "Rp${transaction.total}",
            color =
            when (transaction.type) {
                TransactionType.INCOME -> MaterialTheme.colorScheme.tertiaryContainer
                TransactionType.OUTCOME -> MaterialTheme.colorScheme.onTertiaryContainer
                else -> MaterialTheme.colorScheme.primary
            },
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.End,
            modifier = Modifier.fillMaxWidth()
        )


        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = true,
                onCheckedChange = {
                    // todo
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.onTertiary,
                    checkmarkColor = Color.White
                ),

                //below line is uses an interaction source
                // that handles interaction events for the checkbox
                interactionSource = remember { MutableInteractionSource() }
            )
            Text(
                "Revisi Nanti",
//                fontSize = 12.sp,
            )

            Checkbox(
                checked = false,
                onCheckedChange = {
                    // todo
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.onTertiary,
                    checkmarkColor = Color.White
                ),

                //below line is uses an interaction source
                // that handles interaction events for the checkbox
                interactionSource = remember { MutableInteractionSource() }
            )
            Text(
                "kesalahan Transkripsi",
//                fontSize = 12.sp,
            )
        }

        Button(
            onClick = {},
            Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonColors(
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.primary,
                disabledContentColor = transpernt,
                disabledContainerColor = transpernt
            ),

        ) {

            Text(
                "Simpan",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}


@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun ProposedTransactionPreview(
    @PreviewParameter(TransactionItemPreviewParameterProvider::class) state : TransactionItemState
) {
    SpeechnancialTheme {
        Surface {
            ProposedTransaction(
                transaction = state.transaction,
                isExpanded = state.isExpanded,
                onItemClick = {},
                onDropdownClick = {}
            )
        }
    }
}