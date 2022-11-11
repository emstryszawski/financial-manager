package pl.edu.pjatk.financialmanager

import java.math.BigDecimal
import java.time.LocalDate

data class Transaction(
    val id: Long,
    val title: String,
    val value: BigDecimal,
    val category: String,
    val date: LocalDate
) {
    fun isExpense(): Boolean = value < BigDecimal.ZERO

    fun isIncome(): Boolean = value > BigDecimal.ZERO
}