package com.example.speechnancial.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.speechnancial.R
import com.example.speechnancial.viewmodel.inputTransactionScreen.InputTransactionEvent
import com.example.speechnancial.viewmodel.inputTransactionScreen.InputTransactionState
import com.example.speechnancial.ui.component.CustomCheckbox
import com.example.speechnancial.ui.component.TransactionDisplayerItem
import com.example.speechnancial.ui.navigation.TransactionListScreenNavigation
import com.example.speechnancial.tools.inputTransactionScreen.recordTransactionHandler
import com.example.speechnancial.tools.inputTransactionScreen.resetInput

@Composable
fun InputTransactionScreen(
    navController: NavController,
    state: InputTransactionState,
    onEvent: (InputTransactionEvent) -> Unit
) {
    Column {
        Text(
            text =
            if (state.source.value.isEmpty() && state.previousPartialResult.value.isEmpty())
                "Transkripsi Anda akan tampil disini"
            else state.source.value + state.previousPartialResult.value
        )

        OutlinedButton(
            onClick = { recordTransactionHandler(state) },
            modifier = Modifier
                .size(150.dp)
                .padding(24.dp),  //avoid the oval shape
            shape = CircleShape,
            border = BorderStroke(1.dp, if (state.isTranscribing.value) Color.Green else Color.Gray),
            contentPadding = PaddingValues(0.dp),  //avoid the little icon
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Blue)
        ) {
            Icon(
                painterResource(R.drawable.ic_mic),
                contentDescription = "microphone",
                tint = if (state.isTranscribing.value) Color.Green else Color.Gray
            )
        }

        CustomCheckbox(
            isChecked = state.transaction.value.isNeedRevise,
            text = "Edit Later?",
            isEnabled = state.isFinishedTranscribing.value,
            onCheckedChange = { newValue ->
                state.updateTransaction { copy(isNeedRevise = newValue) }
            }
        )

        if (state.source.value.isEmpty() && state.previousPartialResult.value.isEmpty())
            Text("Transaksi sah akan muncul disini")
        else
            TransactionDisplayerItem(
                state.transaction.value,
                onItemClick = { }
            )

        Button(
            onClick = { resetInput(state) },
            enabled = state.isFinishedTranscribing.value
        ) {
            Text("Reset")
        }

        Button(
            onClick = {
                onEvent(InputTransactionEvent.SaveTransaction(state.transaction.value))
                navController.navigate(TransactionListScreenNavigation)
            },
            enabled = state.isFinishedTranscribing.value
        ) {
            Text("Finished")
        }

        Button(onClick = {
            navController.navigate(TransactionListScreenNavigation)
        }) {
            Text("Transaction List")
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun SpeechToTransactionPreview() {
//    val state = rememberSpeechToTransactionState()
//    val navController = rememberNavController()
//    SpeechnancialTheme { SpeechToTransaction(state, navController, null) }
//}

// cant be previewed

//@Preview(showBackground = true)
//@Composable
//fun InputTransactionScreenPreview() {
//
//    val context = LocalContext.current
//    val p : SpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
//    SpeechnancialTheme {
//
//        val dummyStringState = remember { mutableStateOf("") }
//        val dummyBoolState = remember { mutableStateOf(false) }
//        val speechRecognizer = remember { mutableStateOf(p) }
//        val dummySpeechRecognizerIntentState = remember { mutableStateOf(Intent()) }
//        val dummyTransacrionState = remember { mutableStateOf(Transaction())}
//
//        val dummyState = SpeechToTransactionState(
//            context = context,
//            recordAudioPermissionResultLauncher = DummyActivityResultLauncher(
//                DummyPermissionContract()
//            ),
//            previousPartialResult = dummyStringState,
//            source = dummyStringState,
//            splittedSource = mutableListOf(),
//            transaction = dummyTransacrionState,
//            isTranscribing = dummyBoolState,
//            speechRecognizer = speechRecognizer,
//            speechRecognizerIntent = dummySpeechRecognizerIntentState
//        )
//        val navController = rememberNavController()
//        InputTransactionScreen(navController, dummyState)
//    }
//}
//
//// Dummy implementation of ActivityResultContract
//class DummyPermissionContract : ActivityResultContract<String, Boolean>() {
//    override fun createIntent(context: Context, input: String): Intent {
//
//    }
//
//    override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
//        return true // Assume permission is granted in the dummy contract
//    }
//}
//
//// Dummy implementation for ActivityResultLauncher for preview
//class DummyActivityResultLauncher(override val contract: ActivityResultContract<String, *>) : ActivityResultLauncher<String>() {
//    override fun launch(input: String, options: ActivityOptionsCompat?) {}
//    override fun unregister() {}
//}
