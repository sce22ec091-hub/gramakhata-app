package com.example.gramakhataapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gramakhataapp.data.Customer
import com.example.gramakhataapp.data.TransactionEntry
import com.example.gramakhataapp.data.TransactionType
import com.example.gramakhataapp.repository.LedgerRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CustomerDetailViewModel(
    private val repository: LedgerRepository,
    private val customerId: Int
) : ViewModel() {

    private val _customer = MutableStateFlow<Customer?>(null)
    val customer: StateFlow<Customer?> = _customer.asStateFlow()

    val transactions: StateFlow<List<TransactionEntry>> = repository.getTransactionsForCustomer(customerId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        loadCustomer()
    }

    private fun loadCustomer() {
        viewModelScope.launch {
            _customer.value = repository.getCustomerById(customerId)
        }
    }

    /**
     * Records a new transaction (Koduvudu or Tegedukolluvudu)
     */
    fun addTransaction(amount: Double, type: TransactionType, note: String? = null) {
        viewModelScope.launch {
            val transaction = TransactionEntry(
                customerId = customerId,
                amount = amount,
                type = type,
                note = note,
                timestamp = System.currentTimeMillis()
            )
            repository.addTransaction(transaction)
            
            // Refresh customer data to reflect new balance
            loadCustomer()
        }
    }
}
