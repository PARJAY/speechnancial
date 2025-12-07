package com.example.speechnancial.newUi.screen.inputTransactionScreen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.speechnancial.MyApp
import com.example.speechnancial.R
import com.example.speechnancial.data.firebase.model.TransactionType
import com.example.speechnancial.data.firebase.viewmodel.InputTransactionEvent
import com.example.speechnancial.data.firebase.viewmodel.InputTransactionState
import com.example.speechnancial.newUi.component.ProposedTransaction
import com.example.speechnancial.newUi.component.dialogBox.DialogSelectWallet
import com.example.speechnancial.newUi.component.dialogBox.DialogTransactionKindPicker
import com.example.speechnancial.newUi.component.dialogBox.RelatedItemPickerDialog
import com.example.speechnancial.newUi.component.dialogBox.SelectTransactionDateDialog
import com.example.speechnancial.newUi.component.sharedComponent.CompactFAB
import com.example.speechnancial.tools.onFocusLost

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
//                    // kalok vm kosong, update state dan vm
                    if (
                        inputTransactionState.source.isEmpty()
                        && inputTransactionState.previousPartialResult.isEmpty()
                    ) {
                        tempString.value = it
                        onEvent(InputTransactionEvent.HandleUserInput(tempString.value))
                        Log.d("ITScreen", "if 1 - tempString.value : ${tempString.value}")
                    } else {
                        Log.d("ITScreen", "if 1 - passed : ${tempString.value}")
                    }

                    // kalok vm isi, state = vm, baru update vm
                    if (tempString.value != inputTransactionState.source) {
                        tempString.value = inputTransactionState.source
                        tempString.value = it
                        onEvent(InputTransactionEvent.HandleUserInput(tempString.value))
                        Log.d("ITScreen", "if 2 - tempString.value : ${tempString.value}")
                    } else {
                        Log.d("ITScreen", "if 2 - passed : ${tempString.value}")
                    }

                    // kalok udah sinkron, update state dan vm barengan
                    if (tempString.value == inputTransactionState.source) {
                        tempString.value = it
                        onEvent(InputTransactionEvent.HandleUserInput(tempString.value))
                        Log.d("ITScreen", "if 3 - tempString.value : ${tempString.value}")
                    } else {
                        Log.d("ITScreen", "if 3 - passed : ${tempString.value}")
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
                            onEvent(InputTransactionEvent.OnFinishInputtingMakeTransactions)
                            focusManager.clearFocus()
                        }
                    ),
                shape = RoundedCornerShape(8.dp),

                label = {
                    if (inputTransactionState.source.isEmpty() || inputTransactionState.previousPartialResult.isEmpty())
                        Text(
                            "Input Dengan Bicara atau Mengetik. Transaksi yang diinputkan dapat lebih dari 1",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.secondary,
                            fontSize = 18.sp,
                        )
                },
            )
        }

        Row (
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            if (inputTransactionState.proposedTransactions.isNotEmpty()) {
                CompactFAB(
                    icons = Icons.Filled.Refresh,
                    isActive = false,
                    onClick = {
                        onEvent(InputTransactionEvent.ResetInput)
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

        if (inputTransactionState.proposedTransactions.isNotEmpty()) {
            val currentTransaction = inputTransactionState.proposedTransactions.getOrNull(0)
            ProposedTransaction(
                proposedTransactions = inputTransactionState.proposedTransactions,
                isEditExistingTransaction = inputTransactionState.proposedTransactions[inputTransactionState.currentTransactionPosition].transaction.uuid.isNotEmpty(),
                onConfirmButtonClick = {
                    if (inputTransactionState.proposedTransactions[0].transaction.fullText.isNotEmpty()) {
                        onEvent(InputTransactionEvent.SaveTransaction)
                        navController.navigateUp()
                    }
                    else {
                        Toast.makeText(MyApp.appModule.context, "Input Belum Dimasukkan", Toast.LENGTH_SHORT).show()
                    }
                },

                onDeleteButtonClick = {
                    onEvent(InputTransactionEvent.DeleteTransaction)
                    navController.navigateUp()
                },
                onUpdateButtonClick = {
                    onEvent(InputTransactionEvent.UpdateTransaction)
                    navController.navigateUp()
                },
                onEvent = onEvent,

                currentPosition = inputTransactionState.currentTransactionPosition,
                onIsNeedReviseChanged = { isChecked ->
                    // Assuming you want to update the current proposed transaction's isNeedRevise
                    currentTransaction?.let {
//                        val updatedTransaction = it.copy(isNeedRevise = isChecked)
                        // You'll likely need a way to update this in your ViewModel's state
                        // For now, we'll just call an event if you have one
                        // onEvent(InputTransactionEvent.UpdateProposedTransaction(updatedTransaction))
                    }
                },
                isNeedRevise = false
            )
        }
    }

    // Dialogs
    DialogTransactionKindPicker(
        showDialog = inputTransactionState.showTransactionKindDialog,
        onDismiss = { onEvent(InputTransactionEvent.HideTransactionKindDialog) },
        onConfirm = { transactionKind, transactionType ->
            val currentTransaction = inputTransactionState.proposedTransactions.getOrNull(inputTransactionState.currentTransactionPosition)?.transaction
            if (currentTransaction?.transactionTypeOrdinal != transactionKind.ordinal) {
                onEvent(InputTransactionEvent.SelectTransactionKind(transactionKind))
            }
        }
    )

    if (inputTransactionState.showSelectWalletDialog) {
        DialogSelectWallet(
            onDismissRequest = { onEvent(InputTransactionEvent.HideSelectWalletDialog) },
            title = "Pilih Dompet",
            wallets =
            if (inputTransactionState.proposedTransactions.getOrNull(inputTransactionState.currentTransactionPosition)?.transaction?.transactionTypeOrdinal != TransactionType.REGULAR_TRANSFER.ordinal) inputTransactionState.wallets
            else inputTransactionState.wallets.filter { it.uuid != inputTransactionState.proposedTransactions.getOrNull(inputTransactionState.currentTransactionPosition)?.transaction?.relatedEntityId },
            selectedWallet = inputTransactionState.wallets.find {
                it.uuid == inputTransactionState.proposedTransactions.getOrNull(inputTransactionState.currentTransactionPosition)?.transaction?.relatedWalletUuid
            },
            onConfirmation = { wallet ->
                onEvent(InputTransactionEvent.SelectWallet(wallet))
            }
        )
    }

    RelatedItemPickerDialog(
        showDialog = inputTransactionState.showRelatedItemDialog,
        onDismiss = { onEvent(InputTransactionEvent.HideRelatedItemDialog) },
        transactionType = inputTransactionState.proposedTransactions.getOrNull(inputTransactionState.currentTransactionPosition)?.transaction?.transactionTypeOrdinal?.let { TransactionType.entries.getOrNull(it) },
        categories = inputTransactionState.categories,
        savings = inputTransactionState.savings,
        debtAndReceivable = inputTransactionState.debtReceivables,
        wallets =
            if (inputTransactionState.proposedTransactions.getOrNull(inputTransactionState.currentTransactionPosition)?.transaction?.transactionTypeOrdinal != TransactionType.REGULAR_TRANSFER.ordinal) inputTransactionState.wallets
            else inputTransactionState.wallets.filter { it.uuid != inputTransactionState.proposedTransactions.getOrNull(inputTransactionState.currentTransactionPosition)?.transaction?.relatedWalletUuid },
        onConfirm = { relatedKindUUID, relatedKindName ->
            onEvent(InputTransactionEvent.SelectRelatedItem(relatedKindUUID, relatedKindName))
        }
    )

    SelectTransactionDateDialog(
        showDialog = inputTransactionState.showDatePickerDialog,
        onDismiss = { onEvent(InputTransactionEvent.HideDatePickerDialog) },
        onDateSelected = { localDate ->
            onEvent(InputTransactionEvent.SelectDate(localDate))
        }
    )
}

