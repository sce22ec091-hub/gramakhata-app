package com.example.gramakhataapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gramakhataapp.data.Customer
import com.example.gramakhataapp.repository.LedgerRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DashboardViewModel(private val repository: LedgerRepository) : ViewModel() {

    // Expose customers as a StateFlow for the UI to observe (UI-reactive)
    val customers: StateFlow<List<Customer>> = repository.allCustomers
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addCustomer(name: String, phone: String, photoUri: String? = null) {
        viewModelScope.launch {
            val newCustomer = Customer(
                name = name,
                phoneNumber = phone,
                photoUri = photoUri
            )
            repository.addCustomer(newCustomer)
        }
    }
}
