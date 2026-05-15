package com.example.gramakhataapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomerDao {
    @Query("SELECT * FROM customers ORDER BY totalDebt DESC, lastTransactionDate ASC")
    fun getCustomersByPriority(): Flow<List<Customer>>

    @Query("SELECT * FROM customers WHERE id = :id")
    suspend fun getCustomerById(id: Int): Customer?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomer(customer: Customer): Long

    @Update
    suspend fun updateCustomer(customer: Customer)

    @Delete
    suspend fun deleteCustomer(customer: Customer)

    @Query("UPDATE customers SET totalDebt = totalDebt + :changeAmount, lastTransactionDate = :timestamp WHERE id = :customerId")
    suspend fun updateBalance(customerId: Int, changeAmount: Double, timestamp: Long)
}
