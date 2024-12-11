package com.example.speechnancial.tools

import android.Manifest
import android.content.Intent
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.speechnancial.data.model.Transaction
import com.example.speechnancial.viewmodel.PermissionRequestViewModel
import com.example.speechnancial.viewmodel.inputTransactionScreen.InputTransactionState
import java.util.Locale

@Composable
fun rememberSpeechToTransactionState(): InputTransactionState {
    val context = LocalContext.current
    val viewModel = viewModel<PermissionRequestViewModel>()

    val recordAudioPermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            viewModel.onPermissionResult(
                permission = Manifest.permission.RECORD_AUDIO,
                isGranted = isGranted
            )
        }
    )

    val previousPartialResult = remember { mutableStateOf("") }
    val source = remember { mutableStateOf("") }
    val transactionResult = remember { mutableStateOf(Transaction()) }
    val isTranscribing = remember { mutableStateOf(false) }
    val isFinishedTranscribing = remember { mutableStateOf(false) }

    val speechRecognizer = remember { mutableStateOf(SpeechRecognizer.createSpeechRecognizer(context)) }
    val speechRecognizerIntent = remember {
        mutableStateOf(
            Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
                putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
                putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 7500)
                putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 7500)
            }
        )
    }

    return InputTransactionState(
        context,
        recordAudioPermissionResultLauncher,
        previousPartialResult,
        source,
        transactionResult,
        isTranscribing,
        isFinishedTranscribing,
        speechRecognizer,
        speechRecognizerIntent
    )
}