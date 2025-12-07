package com.example.speechnancial.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.speechnancial.data.datastore.WalletDataStoreManager
import com.example.speechnancial.data.datastore.dataStore
import com.example.speechnancial.data.db.AppDatabase
import com.example.speechnancial.data.firebase.repository.BudgetRepository
import com.example.speechnancial.data.firebase.repository.CategoryRepository
import com.example.speechnancial.data.firebase.repository.DebtAndReceivableRepository
import com.example.speechnancial.data.firebase.repository.SavingRepository
import com.example.speechnancial.data.firebase.repository.TransactionDetailRepository
import com.example.speechnancial.data.firebase.repository.TransactionRepository
import com.example.speechnancial.data.firebase.repository.UserRepositoryImpl
import com.example.speechnancial.data.firebase.repository.WalletRepository
import com.google.firebase.firestore.FirebaseFirestore

class AppModuleImpl(
    private val appContext: Context,
    private val application: Application
): AppModule {

    override val database: AppDatabase by lazy {
        Room.databaseBuilder(appContext, AppDatabase::class.java, "transaction.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    override val wallet: WalletDataStoreManager by lazy {
        WalletDataStoreManager.getInstance(application.dataStore)
    }

    override val firestore: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    override val transactionRepositoryImpl: TransactionRepository by lazy {
        TransactionRepository(firestore)
    }

    override val transactionDetailRepositoryImpl: TransactionDetailRepository by lazy {
        TransactionDetailRepository(firestore)
    }

    override val categoryRepositoryImpl: CategoryRepository by lazy {
        CategoryRepository(firestore)
    }

    override val walletRepositoryImpl: WalletRepository by lazy {
        WalletRepository(firestore)
    }

    override val budgetRepositoryImpl: BudgetRepository by lazy {
        BudgetRepository(firestore)
    }

    override val savingRepositoryImpl: SavingRepository by lazy {
        SavingRepository(firestore)
    }

    override val debtAndReceivableRepositoryImpl: DebtAndReceivableRepository by lazy {
        DebtAndReceivableRepository(firestore)
    }

    override val userRepositoryImpl: UserRepositoryImpl by lazy {
        UserRepositoryImpl(firestore)
    }

    override val context : Context by lazy {
        appContext
    }
}