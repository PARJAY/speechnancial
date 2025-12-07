//package com.example.speechnancial.newUi.screen.transactionListScreen
//
//import androidx.compose.foundation.BorderStroke
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.Surface
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.tooling.preview.PreviewLightDark
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.example.speechnancial.ui.theme.SpeechnancialTheme
//
//@Composable
//fun ChartItem(
//    totalIncome: Float,
//    totalOutcome: Float,
//    timeRange: String,
//    highestBarValue: Float
//) {
//    val incomeProgress = if (highestBarValue > 0) totalIncome / highestBarValue else 0f
//    val outcomeProgress = if (highestBarValue > 0) totalOutcome / highestBarValue else 0f
//
//    Column(
////        modifier = Modifier.padding(16.dp),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Row(
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            ProgressBarUpDownContainer(
//                progress = incomeProgress,
//                color = Color.Green
//            )
//
//            Spacer(modifier = Modifier.width(4.dp)) // Space between bars
//
//            ProgressBarUpDownContainer(
//                progress = outcomeProgress,
//                color = Color.Red
//            )
//        }
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        Text(
//            text = timeRange,
//            fontSize = 14.sp,
//            modifier = Modifier.align(Alignment.CenterHorizontally) // Center the text
//        )
//    }
//}
//
//@Composable
//fun ProgressBarUpDownContainer(
//    progress: Float,
//    color: Color
//) {
//    val barWidth = 18.dp
//    val barHeight = 200.dp
//
//    Box(
//        modifier = Modifier
//            .width(barWidth)
//            .height(barHeight)
//            .border(
//                BorderStroke(1.dp, Color.Gray),
//                RoundedCornerShape(4.dp)
//            ),
//        contentAlignment = Alignment.BottomCenter
//    ) {
//        Box(
//            modifier = Modifier
//                .fillMaxHeight(progress)
//                .width(barWidth)
//                .background(
//                    color,
//                    RoundedCornerShape(4.dp)
//                )
//        )
//    }
//}
//
//@PreviewLightDark
//@Composable
//fun ChartItemPreview() {
//    SpeechnancialTheme {
//        Surface {
//            ChartItem(
//                totalOutcome = 5000f,
//                totalIncome = 5000f,
//                timeRange = "today",
//                highestBarValue = 10000f
//            )
//        }
//    }
//}