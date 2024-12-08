package com.example.speechnancial.ui.component

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.util.Locale
import androidx.activity.compose.rememberLauncherForActivityResult as rememberLauncherForActivityResult1

@Composable
fun OldSpeechToTextButton(speechText: MutableState<String>) {
    val launcher = rememberLauncherForActivityResult1(ActivityResultContracts.StartActivityForResult())
    { it : ActivityResult ->
        if (it.resultCode == Activity.RESULT_OK)
        {
            val data = it.data
            val result = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)

            speechText.value = result?.get(0) ?: "No speech detected."
        }
        else speechText.value = "[Speech recognition failed.]"
    }

    var isPopUpVisible by remember { mutableStateOf(false) }

    Button(onClick = {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Go on then, say something.")
        launcher.launch(intent)
        isPopUpVisible = true // Display pop-up on launch
    }) {
        Text("Start speech recognition")
    }

    Spacer(modifier = Modifier.padding(16.dp))
    Text(speechText.value)

    // Display pop-up conditionally
    if (isPopUpVisible) {
        // Pop-up content goes here...
        Button(onClick = { isPopUpVisible = false }) { // Close button
            Text("Close")
        }
    }
}