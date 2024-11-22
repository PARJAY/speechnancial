package com.example.speechnancial.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.speechnancial.presentation.speechToTransaction.SpeechToTransactionState
import com.example.speechnancial.ui.component.SpeechToTransaction
import com.example.speechnancial.ui.navigation.TransactionListScreenNavigation

@Composable
fun InputTransactionScreen(
    navController: NavController,
    state: SpeechToTransactionState
) {
    Column {
        SpeechToTransaction(
            state,
            navController
        )

        Button(onClick = {
            navController.navigate(TransactionListScreenNavigation)
        }) {
            Text("Transaction List")
        }
    }
}

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
//            transactionResult = dummyTransacrionState,
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
//        TODO("Not yet implemented")
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
