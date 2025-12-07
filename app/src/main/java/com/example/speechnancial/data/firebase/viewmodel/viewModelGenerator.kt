package com.example.speechnancial.data.firebase.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.speechnancial.MyApp
import com.example.speechnancial.data.firebase.model.Budget
import com.example.speechnancial.data.firebase.model.BudgetRealization

@Composable
fun makeTransactionUIVM(firestoreCollectionViewModel: FirestoreCollectionViewModel, currentUserId: String) : TransactionUIViewModel {
    return viewModel(
        factory = viewModelFactory {
            TransactionUIViewModel(
                firestoreCollectionViewModel,
                MyApp.appModule.userRepositoryImpl,
                currentUserId
            )
        }
    )
}

@Composable
fun makeCategoryUIVM(userId: String, firestoreCollectionViewModel: FirestoreCollectionViewModel) : CategoryUIViewModel {
    return viewModel(
        factory = viewModelFactory {
            CategoryUIViewModel(
                userId,
                firestoreCollectionViewModel,
                MyApp.appModule.categoryRepositoryImpl
            )
        }
    )
}

@Composable
fun makeCategoryDetailUIVM(firestoreCollectionViewModel: FirestoreCollectionViewModel) : CategoryDetailUIViewModel {
    return viewModel(
        factory = viewModelFactory {
            CategoryDetailUIViewModel(firestoreCollectionViewModel)
        }
    )
}

@Composable
fun makeWalletListScreenUIVM(userId: String, firestoreCollectionViewModel: FirestoreCollectionViewModel) : WalletListScreenUIViewModel {
    return viewModel(
        factory = viewModelFactory {
            WalletListScreenUIViewModel(
                userId,
                firestoreCollectionViewModel,
                MyApp.appModule.walletRepositoryImpl
            )
        }
    )
}

@Composable
fun makeWalletDetailUIVM(firestoreCollectionViewModel: FirestoreCollectionViewModel) : WalletDetailScreenUIViewModel {
    return viewModel(
        factory = viewModelFactory {
            WalletDetailScreenUIViewModel(firestoreCollectionViewModel)
        }
    )
}

@Composable
fun makeBudgetUIVM(userId: String, firestoreCollectionViewModel: FirestoreCollectionViewModel) : BudgetListScreenUIViewModel {
    return viewModel(
        factory = viewModelFactory {
            BudgetListScreenUIViewModel(
                userId,
                firestoreCollectionViewModel,
                MyApp.appModule.budgetRepositoryImpl
            )
        }
    )
}

@Composable
fun makeBudgetHistoryUIVM(
    userId: String,
    firestoreCollectionViewModel: FirestoreCollectionViewModel, budget: Budget
): BudgetHistoryViewModel {
    return viewModel(
        factory = viewModelFactory {
            BudgetHistoryViewModel(
                userId,
                firestoreCollectionViewModel,
                MyApp.appModule.budgetRepositoryImpl,
                budget
            )
        }
    )
}

@Composable
fun makeBudgetDetailHistoryUIVM(
    userId: String,
    firestoreCollectionViewModel: FirestoreCollectionViewModel,
    budget: Budget,
    budgetRealization: BudgetRealization // Tambahkan budgetRealization
): BudgetDetailHistoryViewModel {
    return viewModel(
        factory = viewModelFactory {
            BudgetDetailHistoryViewModel(
                userId,
                firestoreCollectionViewModel,
                MyApp.appModule.budgetRepositoryImpl,
                budget,
                budgetRealization // Teruskan budgetRealization
            )
        }
    )
}

@Composable
fun makeBudgetDetailUIVM(firestoreCollectionViewModel: FirestoreCollectionViewModel) : BudgetDetailScreenUIViewModel {
    return viewModel(
        factory = viewModelFactory {
            BudgetDetailScreenUIViewModel(firestoreCollectionViewModel)
        }
    )
}

@Composable
fun makeSavingListUIVM(userId: String, firestoreCollectionViewModel: FirestoreCollectionViewModel) : SavingListScreenUIViewModel {
    return viewModel(
        factory = viewModelFactory {
            SavingListScreenUIViewModel(
                userId,
                firestoreCollectionViewModel,
                MyApp.appModule.savingRepositoryImpl
            )
        }
    )
}

@Composable
fun makeDebtAndReceivableUIVM(userId: String, firestoreCollectionViewModel: FirestoreCollectionViewModel): DebtAndReceivableUIViewModel {
    return viewModel(
        factory = viewModelFactory {
            DebtAndReceivableUIViewModel(
                userId,
                firestoreCollectionViewModel,
                MyApp.appModule.debtAndReceivableRepositoryImpl
            )
        }
    )
}

@Composable
fun makeInputTransactionUIVM(userId: String, firestoreCollectionViewModel: FirestoreCollectionViewModel): InputTransactionViewModel {
    val context = LocalContext.current
    return viewModel(
        factory = viewModelFactory {
                InputTransactionViewModel(
                    userId,
                    firestoreCollectionViewModel = firestoreCollectionViewModel,
                    walletRepository = MyApp.appModule.walletRepositoryImpl,
                    transactionRepository = MyApp.appModule.transactionRepositoryImpl,
                    budgetRepository = MyApp.appModule.budgetRepositoryImpl,
                    savingRepository = MyApp.appModule.savingRepositoryImpl,
                    debtReceivableRepository = MyApp.appModule.debtAndReceivableRepositoryImpl,
                    context = context
                )
        }
    )
}

// buatlah UIVM untuk viewmodel ini
//class InputTransactionViewModel(
//    private val firestoreCollectionViewModel: FirestoreCollectionViewModel,
//    private val walletRepository: WalletRepository,
//    private val transactionRepository: TransactionRepository,
//    private val budgetRepository: BudgetRepository,
//    private val savingRepository: SavingRepository,
//    private val debtReceivableRepository: DebtAndReceivableRepository,
//    context: Context,
//)
