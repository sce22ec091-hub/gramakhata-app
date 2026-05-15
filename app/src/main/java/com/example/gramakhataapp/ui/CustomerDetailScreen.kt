package com.example.gramakhataapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gramakhataapp.data.TransactionEntry
import com.example.gramakhataapp.data.TransactionType
import com.example.gramakhataapp.viewmodel.CustomerDetailViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerDetailScreen(
    viewModel: CustomerDetailViewModel,
    onBack: () -> Unit
) {
    val customer by viewModel.customer.collectAsState()
    val transactions by viewModel.transactions.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(customer?.name ?: "Customer Details") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Call logic */ }) {
                        Icon(Icons.Default.Phone, contentDescription = "Call", tint = DeepGreen)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SoftCream)
            )
        },
        bottomBar = {
            TransactionBottomBar(
                onGive = { amount, note -> viewModel.addTransaction(amount, TransactionType.KODUVUDU, note) },
                onTake = { amount, note -> viewModel.addTransaction(amount, TransactionType.TEGEDUKOLLUVUDU, note) }
            )
        },
        containerColor = SoftCream
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            // Balance Summary Header
            BalanceHeader(customer?.totalDebt ?: 0.0)

            // Ledger List
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 16.dp, top = 8.dp)
            ) {
                items(transactions) { transaction ->
                    TransactionItem(transaction)
                }
            }
        }
    }
}

@Composable
fun BalanceHeader(balance: Double) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = if (balance > 0) Color(0xFFFFEBEE) else Color(0xFFE8F5E9)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Total Balance", fontSize = 14.sp, color = Color.Gray)
            Text(
                "₹$balance",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = if (balance > 0) DebtRed else PaymentGreen
            )
            Text(
                if (balance > 0) "Customer owes you" else "You owe customer",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = if (balance > 0) DebtRed else PaymentGreen
            )
        }
    }
}

@Composable
fun TransactionItem(transaction: TransactionEntry) {
    val isGive = transaction.type == TransactionType.KODUVUDU
    val dateFormat = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault())

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                if (isGive) "Items Given (Debit)" else "Payment Received (Credit)",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = if (isGive) DebtRed else PaymentGreen
            )
            Text(
                dateFormat.format(Date(transaction.timestamp)),
                fontSize = 12.sp,
                color = Color.Gray
            )
            if (!transaction.note.isNullOrBlank()) {
                Text(transaction.note!!, fontSize = 12.sp, color = Color.DarkGray)
            }
        }

        Text(
            "₹${transaction.amount}",
            fontSize = 18.sp,
            fontWeight = FontWeight.ExtraBold,
            color = if (isGive) DebtRed else PaymentGreen
        )
    }
}

@Composable
fun TransactionBottomBar(
    onGive: (Double, String?) -> Unit,
    onTake: (Double, String?) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 8.dp,
        color = Color.White
    ) {
        Row(
            modifier = Modifier.padding(16.dp).navigationBarsPadding(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = { onGive(100.0, "Sample Note") }, // Replace with dialog logic
                modifier = Modifier.weight(1f).height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DebtRed),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("KODUVUDU (GIVE)", fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = { onTake(100.0, "Sample Note") }, // Replace with dialog logic
                modifier = Modifier.weight(1f).height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PaymentGreen),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("TEGEDU... (TAKE)", fontWeight = FontWeight.Bold)
            }
        }
    }
}
