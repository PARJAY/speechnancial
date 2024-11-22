package com.example.speechnancial.tools

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.SpeechRecognizer
import android.util.Log
import android.widget.Toast

fun startSpeechToText(
    context: Context,
    speechRecognizer: SpeechRecognizer,
    speechRecognizerIntent: Intent,
    onPartialResults: (String) -> Unit
) {
    var previousPartialResult = ""

    speechRecognizer.setRecognitionListener(object : RecognitionListener {
        override fun onReadyForSpeech(bundle: Bundle?) {}
        override fun onBeginningOfSpeech() {}
        override fun onRmsChanged(v: Float) {}
        override fun onBufferReceived(bytes: ByteArray?) {}
        override fun onEndOfSpeech() {}
        override fun onError(i: Int) {
            val message = when (i) {
                SpeechRecognizer.ERROR_AUDIO -> "Audio recording error"
                SpeechRecognizer.ERROR_CLIENT -> "Client side error"
                SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Insufficient permissions"
                else -> "Unknown error"
            }
            Toast.makeText(context, "Error occurred: $message", Toast.LENGTH_SHORT).show()
        }

        override fun onResults(bundle: Bundle) {
//            val result = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
//            if (result != null && result[0] != null) {
//                onResults(result[0])
//                Log.d("onResults", result[0])
//            }
//            Log.d("onResults", "result : " + result?.joinToString(prefix = "[",
//                separator = ":",
//                postfix = "]",))
            speechRecognizer.startListening(speechRecognizerIntent)
        }

        override fun onPartialResults(bundle: Bundle) {
            val partialResults = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)

            // TODO : this "partialResults.size > 0" isnt checked yet, may result to error
            if (partialResults != null && partialResults.size > 0) {
                onPartialResults(partialResults[0])
                Log.d("partialResults", partialResults[0])
            }
//            Log.d("partialResults", "partialResults : " + partialResults?.joinToString(prefix = "[",
//                separator = ":",
//                postfix = "]",))
        }

        override fun onEvent(i: Int, bundle: Bundle?) {}
    })
}