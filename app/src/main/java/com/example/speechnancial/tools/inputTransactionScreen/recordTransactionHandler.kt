package com.example.speechnancial.tools.inputTransactionScreen

import android.Manifest
import com.example.speechnancial.viewmodel.inputTransactionScreen.InputTransactionState
import com.example.speechnancial.tools.createTransactionFromInput
import com.example.speechnancial.tools.startSpeechToText

fun recordTransactionHandler(state: InputTransactionState) {
    state.recordAudioPermissionResultLauncher.launch(Manifest.permission.RECORD_AUDIO)

    if (!state.isTranscribing.value) {
        state.isTranscribing.value = true
        state.isFinishedTranscribing.value = false
        startSpeechToText(
            state.context,
            state.speechRecognizer.value,
            state.speechRecognizerIntent.value,
            onPartialResults = {
                if (state.previousPartialResult.value.isNotEmpty() && it.isEmpty()) {
                    state.source.value += state.previousPartialResult.value
//                    Log.d("source", state.source.value)
                }

                state.previousPartialResult.value = it
//                Log.d("previousPartialResult", it)
            }
        )
        state.speechRecognizer.value.startListening(state.speechRecognizerIntent.value)
    } else {
        state.isTranscribing.value = false
        state.isFinishedTranscribing.value = true
        state.speechRecognizer.value.stopListening()
        state.speechRecognizer.value.destroy()

        state.transaction.value = createTransactionFromInput(
            (state.source.value + state.previousPartialResult.value).lowercase()
        )
    }
}