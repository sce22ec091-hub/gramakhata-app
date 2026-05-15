package com.example.gramakhataapp.repository

import com.example.gramakhataapp.data.Customer
import com.example.gramakhataapp.data.CustomerDao
import com.example.gramakhataapp.data.TransactionDao
import com.example.gramakhataapp.data.TransactionEntry
import kotlinx.coroutines.flow.Flow

class LedgerRepository(
    private val customerDao: CustomerDao,
    private val transactionDao: TransactionDao
) {
    val allCustomers: Flow<List<Customer>> = customerDao.getCustomersByPriority()

    suspend fun addCustomer(customer: Customer) {
        customerDao.insertCustomer(customer)
    }

    suspend fun getCustomerById(id: Int): Customer? {
        return customerDao.getCustomerById(id)
    }

    fun getTransactionsForCustomer(customerId: Int): Flow<List<TransactionEntry>> {
        return transactionDao.getTransactionsForCustomer(customerId)
    }

    suspend fun addTransaction(transaction: TransactionEntry) {
        // 1. Insert the transaction record
        transactionDao.insertTransaction(transaction)
        
        // 2. Update the customer's total balance and last transaction timestamp
        // Koduvudu (Give) increases debt (+), Tegedukolluvudu (Take) decreases debt (-)
        val amountChange = if (transaction.type.name == "KODUVUDU") {
            transaction.amount
        } else {
            -transaction.amount
        }
        
        customerDao.updateBalance(transaction.customerId, amountChange, transaction.timestamp)
    }
}
