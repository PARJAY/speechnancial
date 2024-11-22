package com.example.speechnancial.tools

import android.Manifest
import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.speechnancial.data.model.Transaction
import com.example.speechnancial.presentation.PermissionRequestViewModel
import com.example.speechnancial.presentation.speechToTransaction.SpeechToTransactionState
import java.util.Locale

@Composable
fun rememberSpeechToTransactionState(): SpeechToTransactionState {
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
    val splittedSource = remember { mutableListOf<String>() }
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

    return SpeechToTransactionState(
        context,
        recordAudioPermissionResultLauncher,
        previousPartialResult,
        source,
        splittedSource,
        transactionResult,
        isTranscribing,
        isFinishedTranscribing,
        speechRecognizer,
        speechRecognizerIntent
    )
}