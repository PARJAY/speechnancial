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

    private val WALLET_KEY = floatPreferencesKey("wallet")
    private val TOTAL_OUTCOME = floatPreferencesKey("total-outcome")
    private val TOTAL_INCOME = floatPreferencesKey("total-income")

    private fun getWalletBalance(): Flow<Float> {
        return dataStore.data.map { preferences ->
            preferences[WALLET_KEY] ?: 0f
        }
    }

    fun getTotalOutcome(): Flow<Float> {
        return dataStore.data.map { preferences ->
            preferences[TOTAL_OUTCOME] ?: 0f
        }
    }

    fun getTotalIncome(): Flow<Float> {
        return dataStore.data.map { preferences ->
            preferences[TOTAL_INCOME] ?: 0f
        }
    }

    suspend fun income(amount: Float) {
        val currentBalance = getWalletBalance().first()
        val newBalance = currentBalance + amount
        saveWalletBalance(newBalance)
        updateTotalIncome(amount)
    }

    suspend fun expense(amount: Float) {
        val newBalance = getWalletBalance().first() - amount
        saveWalletBalance(newBalance)
        updateTotalOutcome(amount)
    }

    private suspend fun updateTotalIncome(amount: Float) {
        val currentIncome = getTotalIncome().first()
        val newIncome = currentIncome + amount
        dataStore.edit { preferences ->
            preferences[TOTAL_INCOME] = newIncome
        }
    }

    private suspend fun updateTotalOutcome(amount: Float) {
        val currentOutcome = getTotalOutcome().first()
        val newOutcome = currentOutcome + amount
        dataStore.edit { preferences ->
            preferences[TOTAL_OUTCOME] = newOutcome
        }
    }

    private suspend fun saveWalletBalance(balance: Float) {
        dataStore.edit { preferences ->
            preferences[WALLET_KEY] = balance
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