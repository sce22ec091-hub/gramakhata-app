package com.example.gramakhataapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gramakhataapp.ui.theme.*
import com.example.gramakhataapp.viewmodel.DashboardViewModel

@Composable
fun GramaKhataExport(
    viewModel: DashboardViewModel,
    onComplete: () -> Unit
) {
    var step by remember { mutableStateOf(1) }
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = SoftCream
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Step Progress Indicator
            LinearProgressIndicator(
                progress = step / 3f,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .padding(bottom = 32.dp),
                color = DeepGreen,
                trackColor = DeepGreen.copy(alpha = 0.1f)
            )

            when (step) {
                1 -> OnboardingStep(
                    icon = Icons.Default.Person,
                    title = "What is the customer's name?",
                    value = name,
                    onValueChange = { name = it },
                    placeholder = "e.g., Ramesh Gowda",
                    onNext = { if (name.isNotBlank()) step = 2 }
                )
                2 -> OnboardingStep(
                    icon = Icons.Default.Phone,
                    title = "Their mobile number?",
                    value = phone,
                    onValueChange = { phone = it },
                    placeholder = "10-digit mobile number",
                    onNext = { if (phone.length >= 10) step = 3 }
                )
                3 -> {
                    SuccessStep(
                        name = name,
                        onFinish = {
                            viewModel.addCustomer(name, phone)
                            onComplete()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun OnboardingStep(
    icon: ImageVector,
    title: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    onNext: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = DeepGreen
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            title,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(title) },
            placeholder = { Text(placeholder) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = DeepGreen
            )
        )
        Spacer(modifier = Modifier.height(48.dp))
        Button(
            onClick = onNext,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = DeepGreen),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("NEXT", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

@Composable
fun SuccessStep(name: String, onFinish: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            Icons.Default.CheckCircle,
            contentDescription = null,
            modifier = Modifier.size(100.dp),
            tint = PaymentGreen
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            "Account Created!",
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            color = DeepGreen
        )
        Text(
            "Digital Khata for $name is ready.",
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(48.dp))
        Button(
            onClick = onFinish,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = DeepGreen),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("OPEN LEDGER", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}
