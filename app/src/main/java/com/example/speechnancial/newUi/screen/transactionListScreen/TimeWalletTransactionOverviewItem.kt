//package com.example.speechnancial.newUi.screen.transactionListScreen
//
//import androidx.compose.foundation.BorderStroke
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowDropDown
//import androidx.compose.material3.Icon
//import androidx.compose.material3.Surface
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.derivedStateOf
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.tooling.preview.PreviewLightDark
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.example.speechnancial.data.firebase.model.EnumTimeRange
//import com.example.speechnancial.data.firebase.model.Wallet
//import com.example.speechnancial.ui.theme.SpeechnancialTheme
//import com.google.firebase.Timestamp
//
//@Composable
//fun TimeWalletTransactionOverviewItem(
//    walletList: List<Wallet>,
//    onWalletDropdownClicked: () -> Unit,
//    onTimeDropdownClicked: () -> Unit,
//    highestBarValue: Float,
//    chartItems: List<ChartItemData>
//) {
//    val filteredWallets = remember { mutableStateOf(walletList) } // Use a MutableState for filtering
//    val filteredTimeStart = remember { mutableStateOf(Timestamp.now()) }
//
//    val totalBalance = remember { derivedStateOf { filteredWallets.value.sumOf { it.balance.toDouble() }.toFloat() } }
//    val totalIncome = remember { derivedStateOf { filteredWallets.value.sumOf { it.totalEarning.toDouble() }.toFloat() } }
//    val totalOutcome = remember { derivedStateOf { filteredWallets.value.sumOf { it.totalSpending.toDouble() }.toFloat() } }
//
//    val barHeight = 200.dp
//
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(16.dp)
//            .border(
//                BorderStroke(1.dp, Color.Gray),
//                RoundedCornerShape(8.dp)
//            )
//            .padding(16.dp)
//    ) {
//        Row(
//            Modifier.fillMaxWidth(),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Text(
//                text = "${filteredWallets.value.size} Dompet Terpilih", // Access value
//            )
//            Icon(
//                Icons.Default.ArrowDropDown,
//                contentDescription = "Select Wallet",
//                Modifier.clickable { onWalletDropdownClicked() }
//            )
//            Text(
//                text = "Rp ${totalBalance.value}", // Access value
//                fontWeight = FontWeight.Bold,
//                textAlign = TextAlign.End,
//                modifier = Modifier.weight(1f)
//            )
//        }
//
//        Spacer(Modifier.height(4.dp))
//
//        Row(
//            Modifier.fillMaxWidth(),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Text(text = filteredTimeStart.value.seconds.toString()) // Access value
//            Icon(
//                Icons.Default.ArrowDropDown,
//                contentDescription = "Select Time Range",
//                Modifier.clickable { onTimeDropdownClicked() }
//            )
//            Text(
//                text = "Rp ${totalIncome.value}", // Access value
//                color = Color.Green,
//                fontWeight = FontWeight.Bold,
//                textAlign = TextAlign.End,
//                modifier = Modifier
//                    .padding(end = 8.dp)
//                    .weight(1f)
//            )
//            Text(
//                text = "Rp ${totalOutcome.value}", // Access value
//                color = Color.Red,
//                fontWeight = FontWeight.Bold,
//                textAlign = TextAlign.End,
//            )
//        }
//
//        Spacer(Modifier.height(8.dp))
//
//        Row {
//            Column (
//                Modifier.height(barHeight),
//                verticalArrangement = Arrangement.SpaceAround,
//                horizontalAlignment = Alignment.End
//            ){
//                val valueStep = highestBarValue / 5
//                for (i in 0..5) {
//                    Text(
//                        text = (highestBarValue - i * valueStep).toString(),
//                        fontSize = 12.sp
//                    )
//                }
//            }
//
//            Spacer(Modifier.width(8.dp))
//
//            chartItems.forEach { chartItemData ->
//                Column(
//                    modifier = Modifier.weight(1f)
//                ) {
//                    ChartItem(
//                        totalIncome = chartItemData.totalIncome,
//                        totalOutcome = chartItemData.totalOutcome,
//                        timeRange = chartItemData.timeRange,
//                        highestBarValue = highestBarValue
//                    )
//                }
//            }
//        }
//    }
//}
//
//data class ChartItemData(
//    val totalIncome: Float,
//    val totalOutcome: Float,
//    val timeRange: String
//)
//
//@PreviewLightDark
//@Composable
//fun TimeWalletTransactionOverviewItemPreview() {
//    SpeechnancialTheme {
//        Surface {
//            TimeWalletTransactionOverviewItem(
//                walletList = listOf(
//                    Wallet(balance = 100f, totalEarning = 200f, totalSpending = 50f),
//                    Wallet(balance = 200f, totalEarning = 300f, totalSpending = 100f)
//                ),
//                onWalletDropdownClicked = {},
//                onTimeDropdownClicked = {},
//                highestBarValue = 500f,
//                chartItems = listOf(
//                    ChartItemData(150f, 50f, "Week 1"),
//                    ChartItemData(200f, 100f, "Week 2"),
//                    ChartItemData(100f, 25f, "Week 3"),
//                    ChartItemData(180f, 40f, "Week 4"),
//                    ChartItemData(220f, 70f, "Week 5")
//
//                )
//            )
//        }
//    }
//}