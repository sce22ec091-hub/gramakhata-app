package com.example.gramakhataapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gramakhataapp.viewmodel.DashboardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(
    viewModel: DashboardViewModel
) {
    val customers by viewModel.customers.collectAsState()
    val totalOutstanding = customers.sumOf { if (it.totalDebt > 0) it.totalDebt else 0.0 }
    val activeDuesCount = customers.count { it.totalDebt > 0 }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dashboard", fontWeight = FontWeight.Bold, color = DeepGreen) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SoftCream)
            )
        },
        containerColor = SoftCream
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Collection Overview Card
            StatCard(
                title = "Collection Overview",
                content = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Total Outstanding", fontSize = 12.sp, color = Color.Gray)
                            Text(
                                "₹$totalOutstanding",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = DebtRed
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Active Dues", fontSize = 12.sp, color = Color.Gray)
                            Text(
                                "$activeDuesCount",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepGreen
                            )
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Monthly Goal Card
            StatCard(
                title = "Monthly Collection Goal",
                content = {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Collected: ₹0.0", fontSize = 12.sp) // Placeholder
                            Text("Goal: ₹$totalOutstanding", fontSize = 12.sp, color = Color.Gray)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = 0f,
                            modifier = Modifier.fillMaxWidth().height(8.dp),
                            color = DeepGreen,
                            trackColor = DeepGreen.copy(alpha = 0.1f)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Collect older dues to improve cash flow!",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Weekly Activity Placeholder
            StatCard(
                title = "Weekly Activity",
                content = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .background(Color.LightGray.copy(alpha = 0.1f), RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Activity Chart Coming Soon", color = Color.Gray, fontSize = 12.sp)
                    }
                }
            )
        }
    }
}

@Composable
fun StatCard(title: String, content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = DeepGreen)
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}
