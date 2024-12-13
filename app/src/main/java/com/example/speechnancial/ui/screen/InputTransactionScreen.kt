package com.example.speechnancial.ui.screen

import android.content.res.Resources
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.speechnancial.R
import com.example.speechnancial.ui.component.inputTransactionScreen.ProposedTransaction
import com.example.speechnancial.ui.preview.InputTransactionScreenPreviewParameterProvider
import com.example.speechnancial.ui.theme.SpeechnancialTheme
import com.example.speechnancial.viewmodel.inputTransactionScreen.InputTransactionEvent
import com.example.speechnancial.viewmodel.inputTransactionScreen.InputTransactionState

@Composable
fun InputTransactionScreen(
    inputTransactionState: InputTransactionState,
    onEvent: (InputTransactionEvent) -> Unit,
    getRecordAudioPermission: () -> Unit,
) {
    val context = LocalContext.current

    Column {
        Box (
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(horizontal = 24.dp)
                .weight(1f),
        ) {
            // todo belum bisa diketik
            Text(
                text =
                if (inputTransactionState.source.isEmpty() && inputTransactionState.previousPartialResult.isEmpty())
                    "Hasil transkripsi Anda Akan Muncul Disini editable by voice or keyboard"
                else inputTransactionState.source + inputTransactionState.previousPartialResult,
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
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
                },
                onDeleteButtonClick = {
                    onEvent(InputTransactionEvent.DeleteTransaction)
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

@Composable
fun CompactFAB(
    icons: ImageVector,
    onClick: () -> Unit,
    isActive: Boolean,
    contentDescription: String
) {
    FloatingActionButton(
        onClick = { onClick() },
        containerColor =
        if (isActive) MaterialTheme.colorScheme.tertiary
        else MaterialTheme.colorScheme.background,

        contentColor =
        if (isActive) MaterialTheme.colorScheme.background
        else MaterialTheme.colorScheme.primary,

        shape = CircleShape,
        modifier = Modifier
            .padding(vertical = 16.dp)
            .border(
                width = 2.dp,
                MaterialTheme.colorScheme.tertiary,
                shape = CircleShape
            )
    ) {
        Icon(
            icons,
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = contentDescription,
        )
    }
}

@Composable
fun CompactFAB(
    painterResources: Painter,
    onClick: () -> Unit,
    isActive: Boolean,
    contentDescription: String
) {
    FloatingActionButton(
        onClick = { onClick() },
        containerColor =
        if (isActive) MaterialTheme.colorScheme.tertiary
        else MaterialTheme.colorScheme.background,

        contentColor =
        if (isActive) MaterialTheme.colorScheme.background
        else MaterialTheme.colorScheme.primary,

        shape = CircleShape,
        modifier = Modifier
            .padding(vertical = 16.dp)
            .border(
                width = 2.dp,
                MaterialTheme.colorScheme.tertiary,
                shape = CircleShape
            )
    ) {
        Icon(
            painterResources,
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = contentDescription,
        )
    }
}

@PreviewLightDark
@Composable
fun InputTransactionScreenPreview(
    @PreviewParameter(InputTransactionScreenPreviewParameterProvider::class) state : InputTransactionState
) {
    SpeechnancialTheme {
        Surface {
            InputTransactionScreen(
                state,
                onEvent = {},
                getRecordAudioPermission = {}
            )
        }
    }
}