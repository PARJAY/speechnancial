package com.example.speechnancial.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "wallet-balance")

class WalletDataStoreManager private constructor(private val dataStore: DataStore<Preferences>) {

    private val WALLET_BALANCE = floatPreferencesKey("wallet")
    private val TOTAL_OUTCOME = floatPreferencesKey("total-outcome")
    private val TOTAL_INCOME = floatPreferencesKey("total-income")

    fun getWalletBalance(): Flow<Float> {
        return dataStore.data.map { preferences ->
            preferences[WALLET_BALANCE] ?: 0f
        }
    }

    fun getTotalSpending(): Flow<Float> {
        return dataStore.data.map { preferences ->
            preferences[TOTAL_OUTCOME] ?: 0f
        }
    }

    fun getTotalEarning(): Flow<Float> {
        return dataStore.data.map { preferences ->
            preferences[TOTAL_INCOME] ?: 0f
        }
    }


    suspend fun newIncomeInputed(amount: Float) {
        val newTotalEarning = getWalletBalance().first() + amount
        saveWalletBalance(newTotalEarning)
        updateTotalIncome(amount)
    }

    suspend fun newExpenseInputed(amount: Float) {
        val newTotalSpending = getWalletBalance().first() - amount
        saveWalletBalance(newTotalSpending)
        updateTotalOutcome(amount)
    }

    private suspend fun updateTotalIncome(amount: Float) {
        val currentIncome = getTotalEarning().first()
        val newIncome = currentIncome + amount
        dataStore.edit { preferences ->
            preferences[TOTAL_INCOME] = newIncome
        }
    }

    suspend fun setTotalOutcomeAndUpdateBalance(amount: Float) {
        dataStore.edit { preferences ->
            preferences[TOTAL_OUTCOME] = amount
        }

        // Perbarui saldo berdasarkan pendapatan - pengeluaran
        val totalIncome = getTotalEarning().first()
        val newBalance = totalIncome - amount
        saveWalletBalance(newBalance)
    }

    suspend fun setTotalIncomeAndUpdateBalance(amount: Float) {
        dataStore.edit { preferences ->
            preferences[TOTAL_INCOME] = amount
        }

        // Perbarui saldo berdasarkan pendapatan - pengeluaran
        val totalOutcome = getTotalSpending().first()
        val newBalance = amount - totalOutcome
        saveWalletBalance(newBalance)
    }

    suspend fun saveWalletBalance(balance: Float) {
        dataStore.edit { preferences ->
            preferences[WALLET_BALANCE] = balance
        }
    }

    private suspend fun updateTotalOutcome(amount: Float) {
        val currentOutcome = getTotalSpending().first()
        val newOutcome = currentOutcome + amount
        dataStore.edit { preferences ->
            preferences[TOTAL_OUTCOME] = newOutcome
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: WalletDataStoreManager? = null

        fun getInstance(dataStore: DataStore<Preferences>): WalletDataStoreManager {
            return INSTANCE ?: synchronized(this) {
                val instance = WalletDataStoreManager(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}

//val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "wallet-balance")
//
//class WalletDataStoreManager private constructor(private val dataStore: DataStore<Preferences>) {
//
//    private val WALLET_BALLANCE = floatPreferencesKey("wallet")
//    private val TOTAL_OUTCOME = floatPreferencesKey("total-outcome")
//    private val TOTAL_INCOME = floatPreferencesKey("total-income")
//
//    fun getWalletBalance(): Flow<Float> {...}
//    fun getTotalSpending(): Flow<Float> {...}
//    fun getTotalEarning(): Flow<Float> {...}
//
//
//    suspend fun newIncomeInputed(amount: Float) {...}
//    suspend fun newExpenseInputed(amount: Float) {...}
//
//    private suspend fun updateTotalIncome(amount: Float) {...}
//    private suspend fun updateTotalOutcome(amount: Float) {...}
//
//    suspend fun setTotalOutcomeAndUpdateBalance(amount: Float) {...}
//    suspend fun setTotalIncomeAndUpdateBalance(amount: Float) {...}
//
//    suspend fun saveWalletBalance(balance: Float) {...}
//
//    companion object {
//        @Volatile
//        private var INSTANCE: WalletDataStoreManager? = null
//
//        fun getInstance(dataStore: DataStore<Preferences>): WalletDataStoreManager {
//            return INSTANCE ?: synchronized(this) {
//                val instance = WalletDataStoreManager(dataStore)
//                INSTANCE = instance
//                instance
//            }
//        }
//    }
//}