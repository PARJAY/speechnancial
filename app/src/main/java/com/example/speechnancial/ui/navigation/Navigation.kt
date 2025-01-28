package com.example.speechnancial.ui.navigation

import android.Manifest
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.speechnancial.MyApp
import com.example.speechnancial.tools.getRecordAudioPermission
import com.example.speechnancial.ui.screen.InputTransactionScreen
import com.example.speechnancial.ui.screen.TransactionListScreen
import com.example.speechnancial.viewmodel.inputTransactionScreen.InputTransactionEvent
import com.example.speechnancial.viewmodel.makeInputTransactionVM
import com.example.speechnancial.viewmodel.makeTransactionListVM

@Composable
fun Navigation(innerPadding : PaddingValues) {

    val navController = rememberNavController()
    val context = LocalContext.current

    val inputTransactionVM = makeInputTransactionVM(MyApp.appModule.database, context, MyApp.appModule.wallet)

    NavHost(
        navController,
        startDestination = TransactionListScreenNavigation,
        modifier = Modifier.padding(innerPadding)
    ) {
        composable<TransactionListScreenNavigation> {
            val transactionListVM = makeTransactionListVM(MyApp.appModule.database, MyApp.appModule.wallet)
            val state = transactionListVM.state.collectAsStateWithLifecycle().value

            TransactionListScreen(
                navController,
                state,
                transactionListVM::onEvent,
                onItemClickUpdateData = { selectedTransaction ->
                    inputTransactionVM.onEvent(
                        InputTransactionEvent
                            .IsEditExistingTransactionData(transaction = selectedTransaction)
                    )
                }
            )
        }

        composable<InputTransactionScreenNavigation> {
            val state = inputTransactionVM.state.collectAsStateWithLifecycle().value
            val recordAudioPermissionResultLauncher = getRecordAudioPermission()

            InputTransactionScreen(
                navController,
                inputTransactionState = state,
                onEvent = inputTransactionVM::onEvent,
                getRecordAudioPermission = {
                    recordAudioPermissionResultLauncher.launch(Manifest.permission.RECORD_AUDIO)
                }
            )

            BackHandler {
                inputTransactionVM.onEvent(InputTransactionEvent.IsCloseScreen(navController))
                Log.d("InputTransactionScreen :" , "check ${state.isEditExistingTransaction}")
            }
        }
    }
}