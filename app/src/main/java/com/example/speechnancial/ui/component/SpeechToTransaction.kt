package com.example.speechnancial.ui.component

import android.Manifest
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.speechnancial.R
import com.example.speechnancial.presentation.speechToTransaction.SpeechToTransactionState
import com.example.speechnancial.tools.rememberSpeechToTransactionState
import com.example.speechnancial.tools.separator
import com.example.speechnancial.tools.startSpeechToText
import com.example.speechnancial.ui.navigation.TransactionListScreenNavigation
import com.example.speechnancial.ui.theme.SpeechnancialTheme

@Composable
fun SpeechToTransaction(
    state: SpeechToTransactionState,
    navController: NavController
) {
    Text(
        text =
        if (state.source.value.isEmpty() && state.previousPartialResult.value.isEmpty()) "Transkripsi Anda akan tampil disini"
        else state.source.value + state.previousPartialResult.value
    )

    OutlinedButton(
        onClick = {
            state.recordAudioPermissionResultLauncher.launch(Manifest.permission.RECORD_AUDIO)

            if (!state.isTranscribing.value) {
                state.isTranscribing.value = true
                state.isFinishedTranscribing.value = false
                startSpeechToText(
                    state.context,
                    state.speechRecognizer.value,
                    state.speechRecognizerIntent.value,
                    onPartialResults = {
                        if (state.previousPartialResult.value.isNotEmpty() && it.isEmpty())
                            state.source.value += state.previousPartialResult.value

                        state.previousPartialResult.value = it
                    }
                )
                state.speechRecognizer.value.startListening(state.speechRecognizerIntent.value)
            } else {
                state.isTranscribing.value = false
                state.isFinishedTranscribing.value = true
                state.speechRecognizer.value.stopListening()
                state.speechRecognizer.value.destroy()

                state.splittedSource.clear()
                state.splittedSource.addAll(
                    (state.source.value + state.previousPartialResult.value).lowercase().split(" ")
                )

                separator(state.splittedSource, state.transactionResult)
            }
        },
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
        isChecked = state.transactionResult.value.isTranscriptionCorrect,
        text = "isTranscriptionCorrect",
        isEnabled = state.isFinishedTranscribing.value,
        onCheckedChange = { newValue ->
            state.updateTransaction { copy(isTranscriptionCorrect = newValue) }
        }
    )

    CustomCheckbox(
        isChecked = state.transactionResult.value.isNeedRevise,
        text = "isNeedRevise",
        isEnabled = state.isFinishedTranscribing.value,
        onCheckedChange = { newValue ->
            state.updateTransaction { copy(isTranscriptionCorrect = newValue) }
        }
    )

    Button(
        onClick = {
            navController.navigate(TransactionListScreenNavigation)
        },
        enabled = state.isFinishedTranscribing.value
    ) {
        Text("Finished")
    }
}

@Preview(showBackground = true)
@Composable
fun SpeechToTransactionPreview() {
    val state = rememberSpeechToTransactionState()
    val navController = rememberNavController()
    SpeechnancialTheme { SpeechToTransaction(state, navController) }
}