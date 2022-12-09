package pl.edu.pjatk.financialmanager.persistance.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import pl.edu.pjatk.financialmanager.util.CurrencyFormatter
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Entity(tableName = "transaction")
@Parcelize
class Transaction(
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "amount") var amount: BigDecimal,
    @ColumnInfo(name = "category") var category: String?,
    @ColumnInfo(name = "categoryItemPosition") var categoryItemPosition: Int,
    @ColumnInfo(name = "dateOfTransaction") var dateOfTransaction: LocalDateTime,
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Long = 0L
) : Parcelable {

    val formattedAmount: String get() = CurrencyFormatter.toCurrencyFormat(amount)

    val formattedDateOfTransaction: String
        get() = DateTimeFormatter.ofPattern("d MMMM yyyy", CurrencyFormatter.polandLocale)
            .format(dateOfTransaction)

    fun isExpense(): Boolean = amount < BigDecimal.ZERO

    fun isIncome(): Boolean = amount > BigDecimal.ZERO

    override fun toString(): String {
        return "Transakcja o tytule - \"$title\", z dnia $formattedDateOfTransaction na kwotę $formattedAmount"
    }
}