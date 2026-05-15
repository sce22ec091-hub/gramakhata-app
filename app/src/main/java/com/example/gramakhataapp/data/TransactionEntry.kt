package com.example.gramakhataapp.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = Customer::class,
            parentColumns = ["id"],
            childColumns = ["customerId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["customerId"])]
)
data class TransactionEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val customerId: Int,
    val amount: Double,
    val type: TransactionType, // Koduvudu (Give) or Tegedukolluvudu (Take)
    val note: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val voiceNoteUri: String? = null
)

enum class TransactionType {
    KODUVUDU, // RED: Vendor gives items on credit
    TEGEDUKOLLUVUDU // GREEN: Customer pays back money
}
