package com.example.gramakhataapp.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import java.net.URLEncoder

object IntentHelper {

    /**
     * Sends a transaction alert via WhatsApp if installed, else falls back to SMS.
     */
    fun sendTransactionAlert(
        context: Context,
        phoneNumber: String,
        customerName: String,
        amount: Double,
        isCredit: Boolean, // True for Koduvudu (Give), False for Tegedukolluvudu (Take)
        newBalance: Double,
        notes: String? = null,
        upiId: String? = null
    ) {
        val actionText = if (isCredit) "added to your credit" else "received as payment"
        val noteSuffix = if (!notes.isNullOrBlank()) "\nItem: $notes" else ""
        val upiSuffix = if (upiId != null && newBalance > 0) {
            "\n\nPay via UPI:\nupi://pay?pa=$upiId&pn=Vendor&am=$newBalance&cu=INR"
        } else ""

        val message = """
            *Grama-Khata Alert*
            Namaste $customerName,
            
            Amount of ₹$amount has been $actionText.$noteSuffix
            *Current Balance: ₹$newBalance*
            
            Thank you for your business!$upiSuffix
        """.trimIndent()

        sendIntent(context, phoneNumber, message)
    }

    /**
     * Sends a gentle reminder for outstanding debt.
     */
    fun sendGentleReminder(
        context: Context,
        phoneNumber: String,
        customerName: String,
        balance: Double,
        upiId: String? = null
    ) {
        val upiSuffix = if (upiId != null && balance > 0) {
            "\n\nQuick Pay via UPI:\nupi://pay?pa=$upiId&pn=Vendor&am=$balance&cu=INR"
        } else ""

        val message = """
            *Grama-Khata Reminder* 🔔
            Namaste $customerName,
            
            This is a friendly reminder of your outstanding balance of *₹$balance*.
            
            Keeping your account clear helps us serve you better! 🙏
            Thank you!$upiSuffix
        """.trimIndent()

        sendIntent(context, phoneNumber, message)
    }

    private fun sendIntent(context: Context, phoneNumber: String, message: String) {
        val whatsappIntent = Intent(Intent.ACTION_VIEW)
        val url = "https://api.whatsapp.com/send?phone=$phoneNumber&text=" + URLEncoder.encode(message, "UTF-8")
        whatsappIntent.data = Uri.parse(url)
        whatsappIntent.`package` = "com.whatsapp"

        try {
            context.startActivity(whatsappIntent)
        } catch (e: Exception) {
            val smsIntent = Intent(Intent.ACTION_SENDTO)
            smsIntent.data = Uri.parse("smsto:$phoneNumber")
            smsIntent.putExtra("sms_body", message)
            context.startActivity(smsIntent)
        }
    }
}
