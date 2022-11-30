package pl.edu.pjatk.financialmanager.persistance

import androidx.room.TypeConverter
import java.math.BigDecimal
import java.time.LocalDate

class Converters {
    @TypeConverter
    fun fromBigDecimalToString(value: BigDecimal?): String? {
        return value?.toString()
    }

    @TypeConverter
    fun fromStringToBigDecimal(value: String?): BigDecimal? {
        return value?.let { BigDecimal(value) }
    }

    @TypeConverter
    fun fromDateToString(value: LocalDate?): String? {
        return value?.toString()
    }

    @TypeConverter
    fun fromStringToDate(value: String?): LocalDate? {
        return value?.let { LocalDate.parse(it) }
    }
}