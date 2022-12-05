package pl.edu.pjatk.financialmanager.persistance.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity(tableName = "transaction")
class Transaction(
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "amount") var amount: BigDecimal,
    @ColumnInfo(name = "category") var category: String?,
    @ColumnInfo(name = "dateOfTransaction") var dateOfTransaction: LocalDateTime,
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Int = 0
) {

    fun isExpense(): Boolean = amount < BigDecimal.ZERO

    fun isIncome(): Boolean = amount > BigDecimal.ZERO
}