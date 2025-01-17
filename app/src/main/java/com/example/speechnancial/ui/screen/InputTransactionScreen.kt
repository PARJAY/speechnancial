package com.example.speechnancial.ui.screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.speechnancial.R
import com.example.speechnancial.tools.onFocusLost
import com.example.speechnancial.ui.component.inputTransactionScreen.CompactFAB
import com.example.speechnancial.ui.component.inputTransactionScreen.ProposedTransaction
import com.example.speechnancial.ui.preview.InputTransactionScreenPreviewParameterProvider
import com.example.speechnancial.ui.theme.SpeechnancialTheme
import com.example.speechnancial.viewmodel.inputTransactionScreen.InputTransactionEvent
import com.example.speechnancial.viewmodel.inputTransactionScreen.InputTransactionState

@Composable
fun InputTransactionScreen(
    navController: NavHostController,
    inputTransactionState: InputTransactionState,
    onEvent: (InputTransactionEvent) -> Unit,
    getRecordAudioPermission: () -> Unit,
) {
    val context = LocalContext.current

    val tempString = remember {
        mutableStateOf("")
    }

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Column {
        Box (
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(horizontal = 24.dp)
                .pointerInput(Unit) {
                    detectTapGestures {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                    }
                }
                .weight(1f),
            contentAlignment = Alignment.Center,
        ) {
            OutlinedTextField(
                value = inputTransactionState.source + inputTransactionState.previousPartialResult,
                onValueChange = {
                    // kalok vm kosong, update state dan vm
                    if (
                        inputTransactionState.source.isEmpty()
                        && inputTransactionState.previousPartialResult.isEmpty()
                    ) {
                        tempString.value = it
                        onEvent(InputTransactionEvent.HandleUserInput(tempString.value))
//                        Log.d("ITScreen", "if 1 - tempString.value : ${tempString.value}")
                    } else {
//                        Log.d("ITScreen", "if 1 - passed : ${tempString.value}")
                    }

                    // kalok vm isi, state = vm, baru update vm
                    if (tempString.value != inputTransactionState.source) {
                        tempString.value = inputTransactionState.source
                        tempString.value = it
                        onEvent(InputTransactionEvent.HandleUserInput(tempString.value))
//                        Log.d("ITScreen", "if 2 - tempString.value : ${tempString.value}")
                    } else {
//                        Log.d("ITScreen", "if 2 - passed : ${tempString.value}")
                    }

                    // kalok udah sinkron, update state dan vm barengan
                    if (tempString.value == inputTransactionState.source) {
                        tempString.value = it
                        onEvent(InputTransactionEvent.HandleUserInput(tempString.value))
//                        Log.d("ITScreen", "if 3 - tempString.value : ${tempString.value}")
                    } else {
//                        Log.d("ITScreen", "if 3 - passed : ${tempString.value}")
                    }
                },
                enabled = !inputTransactionState.isTranscribing,

                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusLost(
                        onFocusLost = {
                            Toast.makeText(context, "focus lost", Toast.LENGTH_SHORT).show()
                            focusManager.clearFocus()
                        }
                    ),
                shape = RoundedCornerShape(8.dp),

                label = {
                    if (inputTransactionState.source.isEmpty())
                        Text(
                            "Bicara atau Ketik \n Transaksi dapat lebih dari 1",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.secondary,
                            fontSize = 20.sp,
                        )
                },
            )
        }

        Row (
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        ) {
            if (inputTransactionState.proposedTransaction.rawText.isNotEmpty()) {
                CompactFAB(
                    icons = Icons.Filled.Refresh,
                    isActive = false,
                    onClick = {
                        onEvent(InputTransactionEvent.ResetButtonClicked)
                    },
                    contentDescription = "reset input icon"
                )

                Spacer(Modifier.padding(8.dp))
            }

            CompactFAB(
                painterResources = painterResource(R.drawable.ic_mic),
                isActive = inputTransactionState.isTranscribing,
                onClick = {
                    getRecordAudioPermission()
                    onEvent(InputTransactionEvent.SpeechToTransactionButtonClicked(context))
                },
                contentDescription = "mic icon"
            )
        }

        if (inputTransactionState.proposedTransaction.rawText.isNotEmpty())
            ProposedTransaction(
                inputTransactionState.proposedTransaction,
                inputTransactionState.isEditExistingTransaction,

                onConfirmButtonClick = {
                    onEvent(InputTransactionEvent.SaveTransaction)
                    navController.navigateUp()
                },
                onDeleteButtonClick = {
                    onEvent(InputTransactionEvent.DeleteTransaction)
                    navController.navigateUp()
                },
                onUpdateButtonClick = {
                    onEvent(InputTransactionEvent.UpdateTransaction)
                    navController.navigateUp()
                },

                isReviseNeeded = inputTransactionState.isReviseNeeded,
                onReviseNeededClick = {
                    onEvent(InputTransactionEvent.ReviseLaterCheckboxClicked)
                },

                isTransacribtionError = inputTransactionState.isTranscriptionError,
                onTransacribtionErrorClick = {
                    onEvent(InputTransactionEvent.TranscriptionErrorCheckboxClicked)
                }
            )
    }
}

@PreviewLightDark
@Composable
fun InputTransactionScreenPreview(
    @PreviewParameter(InputTransactionScreenPreviewParameterProvider::class) state : InputTransactionState
) {
    SpeechnancialTheme {
        val navController = rememberNavController()
        Surface {
            InputTransactionScreen(
                navController,
                state,
                onEvent = {},
                getRecordAudioPermission = {}
            )
        }
    }
}