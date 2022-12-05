package pl.edu.pjatk.financialmanager.persistance

import androidx.room.TypeConverter
import java.math.BigDecimal
import java.time.LocalDateTime

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
    fun fromDateToString(value: LocalDateTime): String {
        return value.toString()
    }

    @TypeConverter
    fun fromStringToDate(value: String): LocalDateTime {
        return value.let { LocalDateTime.parse(it) }
    }
}