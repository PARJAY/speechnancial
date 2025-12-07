package com.example.speechnancial.newUi.navigation

import android.Manifest
import android.app.Activity.RESULT_OK
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.speechnancial.MyApp
import com.example.speechnancial.data.firebase.auth.GoogleAuthUiClient
import com.example.speechnancial.data.firebase.auth.SignInViewModel
import com.example.speechnancial.data.firebase.model.Budget
import com.example.speechnancial.data.firebase.model.BudgetRealization
import com.example.speechnancial.data.firebase.model.UserModel
import com.example.speechnancial.data.firebase.viewmodel.FirestoreCollectionViewModel
import com.example.speechnancial.data.firebase.viewmodel.InputTransactionEvent
import com.example.speechnancial.data.firebase.viewmodel.makeBudgetDetailHistoryUIVM
import com.example.speechnancial.data.firebase.viewmodel.makeBudgetHistoryUIVM
import com.example.speechnancial.data.firebase.viewmodel.makeBudgetUIVM
import com.example.speechnancial.data.firebase.viewmodel.makeCategoryUIVM
import com.example.speechnancial.data.firebase.viewmodel.makeDebtAndReceivableUIVM
import com.example.speechnancial.data.firebase.viewmodel.makeInputTransactionUIVM
import com.example.speechnancial.data.firebase.viewmodel.makeSavingListUIVM
import com.example.speechnancial.data.firebase.viewmodel.makeTransactionUIVM
import com.example.speechnancial.data.firebase.viewmodel.makeWalletListScreenUIVM
import com.example.speechnancial.newUi.screen.budgetDetailScreen.BudgetHistoryDetailScreen
import com.example.speechnancial.newUi.screen.budgetHistoryScreen.BudgetHistoryScreen
import com.example.speechnancial.newUi.screen.budgetListScreen.BudgetListScreen
import com.example.speechnancial.newUi.screen.categoryListScreen.CategoryListScreen
import com.example.speechnancial.newUi.screen.debtsAndReceivablesListScreen.DebtsAndReceivablesListScreen
import com.example.speechnancial.newUi.screen.inputTransactionScreen.InputTransactionScreen
import com.example.speechnancial.newUi.screen.loginScreen.LoginScreen
import com.example.speechnancial.newUi.screen.savingListScreen.SavingListScreen
import com.example.speechnancial.newUi.screen.signUpScreen.SignUpScreen
import com.example.speechnancial.newUi.screen.transactionListScreen.TransactionListScreen
import com.example.speechnancial.newUi.screen.walletListScreen.WalletListScreen
import com.example.speechnancial.tools.getRecordAudioPermission
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch

@Composable
fun MainScreen(lifecycleOwner: LifecycleOwner) {
    val navController = rememberNavController()
    val signInViewModel = viewModel<SignInViewModel>()
    val firestoreViewModel: FirestoreCollectionViewModel = viewModel()

    val loginState by signInViewModel.state.collectAsStateWithLifecycle()

    val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = MyApp.appModule.context,
            oneTapClient = Identity.getSignInClient(MyApp.appModule.context)
        )
    }

    val lifecycleScope = lifecycleOwner.lifecycleScope

    val selectedBudget = remember { mutableStateOf(Budget()) }
    val selectedBudgetRealization = remember { mutableStateOf(BudgetRealization()) }

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    val bottomBarRoutes = listOf(
        BottomNavItem.KeywordListScreen.route,
        BottomNavItem.CategoryListScreen.route,
        BottomNavItem.TransactionListScreen.route,
        BottomNavItem.WalletListScreen.route,
        BottomNavItem.BudgetListScreen.route,
        BottomNavItem.SavingListScreen.route,
        BottomNavItem.DebtsAndReceivablesListScreen.route
    )

    Scaffold(
        bottomBar = {
            if (bottomBarRoutes.contains(currentRoute)) {
                BottomNavigationBar(navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = LoginScreen,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<LoginScreen> {
                LaunchedEffect(key1 = Unit) {
                    if (googleAuthUiClient.getSignedInUser() != null)
                        navController.navigate(BottomNavItem.TransactionListScreen.route)
                }

                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartIntentSenderForResult(),
                    onResult = { result ->
                        if (result.resultCode == RESULT_OK) {
                            lifecycleScope.launch {
                                val signInResult = googleAuthUiClient.signInWithIntent(
                                    intent = result.data ?: return@launch
                                )
                                signInViewModel.onSignInResult(signInResult)
                            }
                        }
                    }
                )

                LaunchedEffect(key1 = loginState.isSignInSuccessful) {
                    if (!loginState.isSignInSuccessful) return@LaunchedEffect

                    Toast.makeText(MyApp.appModule.context, "Sign in Success", Toast.LENGTH_LONG).show()

                    val userData = signInViewModel.state.value.userData
                    userData?.let { fetchedUser ->
                        val existingUser =
                            MyApp.appModule.userRepositoryImpl.getCustomerById(fetchedUser.userId)
                        Log.d("NAVIGATION : ", "logged in user firebase data : $existingUser")

                        if (existingUser.name.isEmpty()) {
                            val newUser = UserModel(
                                id = fetchedUser.userId,
                                name = fetchedUser.username ?: "",
                            )

                            try {
                                MyApp.appModule.userRepositoryImpl.addOrUpdateCustomer(
                                    fetchedUser.userId,
                                    newUser
                                )
                                Log.d("NAVIGATION : ", "registering user success")
                            } catch (e: Exception) {
                                Log.d("NAVIGATION : ", "failed registering user $e")
                            }
                        }
                    }

                    navController.navigate(BottomNavItem.TransactionListScreen.route) {
                        popUpTo(LoginScreen) { inclusive = true } // agar tidak bisa back ke login
                    }
                    signInViewModel.resetState()
                }

                LoginScreen(
                    state = loginState,
                    onSignInClick = {
                        lifecycleScope.launch {
                            val signInIntentSender = googleAuthUiClient.signIn()
                            launcher.launch(
                                IntentSenderRequest.Builder(
                                    signInIntentSender ?: return@launch
                                ).build()
                            )
                        }
                    },
                )
            }

            composable<SignUpScreen> {
                SignUpScreen(
                    onSignUpSuccess = {
                        navController.navigate(BottomNavItem.TransactionListScreen.route) {
                            popUpTo(LoginScreen) { inclusive = true } // agar tidak bisa back ke login
                            popUpTo(SignUpScreen) { inclusive = true } // agar tidak bisa back ke sign up screen
                        }
                    },
                    onBackToLogin = {
                        navController.popBackStack()
                    }
                )
            }

            composable(BottomNavItem.TransactionListScreen.route) {
                firestoreViewModel.initUserData(googleAuthUiClient.getSignedInUser()!!.userId)

                val transactionViewModel = makeTransactionUIVM(
                    firestoreViewModel,
                    currentUserId = googleAuthUiClient.getSignedInUser().toString()
                )
                val state = transactionViewModel.uiState.collectAsStateWithLifecycle().value

                TransactionListScreen(
                    state,
                    transactionViewModel::onEvent,
                    navController,
                )
            }

            composable(BottomNavItem.CategoryListScreen.route) {
                val categoryUIViewModel = makeCategoryUIVM(googleAuthUiClient.getSignedInUser()!!.userId, firestoreViewModel)
                val state = categoryUIViewModel.uiState.collectAsStateWithLifecycle().value
                // pass the category and keyword state from FirestoreCollectionViewmodel
                // declare the CategoryUI Viewmodel
                // pass the transactionUI state and event
                CategoryListScreen(
                    state,
                    categoryUIViewModel::onEvent
                )
            }

            composable<CategoryDetailScreen> {
                // pass the category and keyword state from FirestoreCollectionViewmodel
                // declare the CategoryDetailUI Viewmodel
                // pass the CategoryDetailUI state and event

//                WalletDetailScreen (
//
//                )
            }

            composable(BottomNavItem.WalletListScreen.route) {
                val walletUIViewModel = makeWalletListScreenUIVM(googleAuthUiClient.getSignedInUser()!!.userId, firestoreViewModel)
                val state = walletUIViewModel.uiState.collectAsStateWithLifecycle().value
                // pass the wallet and keyword state from FirestoreCollectionViewmodel
                // declare the WalletListScreenUI Viewmodel
                // pass the WalletListScreenUI state and event

                WalletListScreen(
                    state,
                    walletUIViewModel::onEvent
                )
            }

            composable<WalletDetailScreen> {
                // Wallet Detail Screen
                // pass the wallet and keyword state from FirestoreCollectionViewmodel
                // declare the WalletDetailScreenUI Viewmodel
                // pass the WalletDetailScreenUI state and event

//                WalletDetailScreen (
//
//                )
            }

            composable(BottomNavItem.BudgetListScreen.route) {
                val budgetUIVM = makeBudgetUIVM(googleAuthUiClient.getSignedInUser()!!.userId, firestoreViewModel)
                val state = budgetUIVM.uiState.collectAsStateWithLifecycle().value

                BudgetListScreen(
                    state,
                    budgetUIVM::onEvent,
                    onBudgetItemClick = {
                        selectedBudget.value = it
                        navController.navigate(BudgetHistoryScreen)
                    }
                )
            }

            composable<BudgetHistoryScreen> {
                val budgetHistoryUIVM = makeBudgetHistoryUIVM(googleAuthUiClient.getSignedInUser()!!.userId, firestoreViewModel, selectedBudget.value)
                val state = budgetHistoryUIVM.uiState.collectAsStateWithLifecycle().value

                BudgetHistoryScreen(
                    navController,
                    state,
                    budgetHistoryUIVM::onEvent,
                    onBudgetHistoryItemClick = { budgetRealization ->
                        selectedBudgetRealization.value = budgetRealization
                        navController.navigate(BudgetHistoryDetailScreen)
                    }
                )
            }

            composable<BudgetHistoryDetailScreen> {
                Log.d("mainscreen", "BudgetDetailHistoryViewModel")
                val budgetDetailHistory = makeBudgetDetailHistoryUIVM(
                    googleAuthUiClient.getSignedInUser()!!.userId,
                    firestoreViewModel,
                    selectedBudget.value,
                    selectedBudgetRealization.value
                )
                val state = budgetDetailHistory.uiState.collectAsStateWithLifecycle().value

                BudgetHistoryDetailScreen(
                    navController,
                    state,
                    budgetDetailHistory::onEvent,
                    onBudgetDetailHistoryItemClick = {
                        // to input transaction screen with clicked transaction data
                    },
                )
            }

            composable(BottomNavItem.SavingListScreen.route) {
                val savingUiVM = makeSavingListUIVM(googleAuthUiClient.getSignedInUser()!!.userId, firestoreViewModel)
                val state = savingUiVM.uiState.collectAsStateWithLifecycle().value

                SavingListScreen(state, savingUiVM::onEvent)
            }


            composable(BottomNavItem.DebtsAndReceivablesListScreen.route) {
                val debtAndReceivableUiVM = makeDebtAndReceivableUIVM(googleAuthUiClient.getSignedInUser()!!.userId, firestoreViewModel)
                val state by debtAndReceivableUiVM.uiState.collectAsStateWithLifecycle()

                DebtsAndReceivablesListScreen(state, debtAndReceivableUiVM::onEvent)
            }

            composable<InputTransactionScreenNav> {
                val inputTransactionUIVM = makeInputTransactionUIVM(googleAuthUiClient.getSignedInUser()!!.userId, firestoreViewModel)
                val state by inputTransactionUIVM.state.collectAsStateWithLifecycle()
                val recordAudioPermissionResultLauncher = getRecordAudioPermission()

                InputTransactionScreen(
                    navController,
                    state,
                    inputTransactionUIVM::onEvent,
                    getRecordAudioPermission = {
                        recordAudioPermissionResultLauncher.launch(Manifest.permission.RECORD_AUDIO)
                    }
                )

                // somehow not working
                BackHandler {
                    inputTransactionUIVM.onEvent(InputTransactionEvent.IsCloseScreen(navController))
                }
            }
        }
    }
}