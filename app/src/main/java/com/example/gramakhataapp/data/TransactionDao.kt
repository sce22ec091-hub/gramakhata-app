package com.example.gramakhataapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions WHERE customerId = :customerId ORDER BY timestamp DESC")
    fun getTransactionsForCustomer(customerId: Int): Flow<List<TransactionEntry>>

    @Insert
    suspend fun insertTransaction(transaction: TransactionEntry): Long

    @Query("SELECT SUM(amount) FROM transactions WHERE customerId = :customerId AND type = 'KODUVUDU'")
    suspend fun getTotalGiven(customerId: Int): Double?

    @Query("SELECT SUM(amount) FROM transactions WHERE customerId = :customerId AND type = 'TEGEDUKOLLUVUDU'")
    suspend fun getTotalTaken(customerId: Int): Double?
}
