package pl.edu.pjatk.financialmanager.persistance

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.util.*

@Entity(tableName = "transaction")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val title: String,
    val value: BigDecimal,
    val category: String?,
    val date: Date?
) {
    fun isExpense(): Boolean = value < BigDecimal.ZERO

    fun isIncome(): Boolean = value > BigDecimal.ZERO
}