package com.example.speechnancial.di

import android.content.Context
import com.example.speechnancial.data.datastore.WalletDataStoreManager
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

interface AppModule {
    val database: AppDatabase
    val wallet: WalletDataStoreManager
    val firestore: FirebaseFirestore
    val userRepositoryImpl : UserRepositoryImpl
    val transactionRepositoryImpl: TransactionRepository
    val transactionDetailRepositoryImpl: TransactionDetailRepository
    val categoryRepositoryImpl: CategoryRepository
    val walletRepositoryImpl: WalletRepository
    val budgetRepositoryImpl: BudgetRepository
    val savingRepositoryImpl: SavingRepository
    val debtAndReceivableRepositoryImpl: DebtAndReceivableRepository
    val context : Context
}