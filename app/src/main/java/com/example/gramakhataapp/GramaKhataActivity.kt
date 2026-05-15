package com.example.gramakhataapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.gramakhataapp.data.AppDatabase
import com.example.gramakhataapp.repository.LedgerRepository
import com.example.gramakhataapp.ui.*
import com.example.gramakhataapp.viewmodel.CustomerDetailViewModel
import com.example.gramakhataapp.viewmodel.DashboardViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Scaffold
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

class GramaKhataActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize dependencies
        val database = AppDatabase.getDatabase(this)
        val repository = LedgerRepository(database.customerDao(), database.transactionDao())

        setContent {
            var currentScreen by remember { mutableStateOf<Screen>(Screen.Login) }

            Scaffold(
                bottomBar = {
                    if (currentScreen !is Screen.Login && currentScreen !is Screen.GramaKhataExport && currentScreen !is Screen.CustomerDetail) {
                        NavigationBar(containerColor = Color.White) {
                            NavigationBarItem(
                                selected = currentScreen is Screen.Dashboard,
                                onClick = { currentScreen = Screen.Dashboard },
                                icon = { Icon(Icons.Default.Home, contentDescription = "Dashboard") },
                                label = { Text("Home") }
                            )
                            NavigationBarItem(
                                selected = currentScreen is Screen.Ledger,
                                onClick = { currentScreen = Screen.Ledger },
                                icon = { Icon(Icons.Default.List, contentDescription = "Ledger") },
                                label = { Text("Ledger") }
                            )
                            NavigationBarItem(
                                selected = currentScreen is Screen.Settings,
                                onClick = { currentScreen = Screen.Settings },
                                icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                                label = { Text("Settings") }
                            )
                        }
                    }
                }
            ) { innerPadding ->
                Box(modifier = Modifier.padding(innerPadding)) {
                    when (val screen = currentScreen) {
                        is Screen.Login -> {
                            LoginScreen(
                                onLoginSuccess = { shopName -> 
                                    currentScreen = Screen.Dashboard 
                                }
                            )
                        }
                        is Screen.Dashboard -> {
                            val viewModel = remember { DashboardViewModel(repository) }
                            AnalyticsScreen(viewModel = viewModel)
                        }
                        is Screen.Ledger -> {
                            val viewModel = remember { DashboardViewModel(repository) }
                            DashboardScreen(
                                viewModel = viewModel,
                                onCustomerClick = { id -> currentScreen = Screen.CustomerDetail(id) },
                                onAddCustomerClick = { currentScreen = Screen.GramaKhataExport }
                            )
                        }
                        is Screen.GramaKhataExport -> {
                            val viewModel = remember { DashboardViewModel(repository) }
                            GramaKhataExport(
                                viewModel = viewModel,
                                onComplete = { currentScreen = Screen.Ledger }
                            )
                        }
                        is Screen.Settings -> {
                            SettingsScreen(
                                onLogout = { currentScreen = Screen.Login }
                            )
                        }
                        is Screen.CustomerDetail -> {
                            val viewModel = remember(screen.id) { 
                                CustomerDetailViewModel(repository, screen.id) 
                            }
                            CustomerDetailScreen(
                                viewModel = viewModel,
                                onBack = { currentScreen = Screen.Ledger }
                            )
                        }
                    }
                }
            }
        }
    }
}

sealed class Screen {
    object Login : Screen()
    object Dashboard : Screen() // Analytics
    object Ledger : Screen()    // Customer List
    object GramaKhataExport : Screen()
    object Settings : Screen()
    data class CustomerDetail(val id: Int) : Screen()
}
