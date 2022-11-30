package pl.edu.pjatk.financialmanager.persistance

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

@Entity(tableName = "transaction")
class Transaction(
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "amount") var amount: BigDecimal,
    @ColumnInfo(name = "category") var category: String?,
    @ColumnInfo(name = "dateOfTransaction") var dateOfTransaction: LocalDate?,
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Int = 0
) {

    fun isExpense(): Boolean = amount < BigDecimal.ZERO

    fun isIncome(): Boolean = amount > BigDecimal.ZERO

    fun getAmountFormatted(): String {
        return amount.toString().format(Locale("pl", "PL"))
    }

    fun setAmount(amount: String): BigDecimal =
        amount.replace("[ zł,.]".toRegex(), "")
            .toBigDecimal()
            .divide(BigDecimal(100))


}