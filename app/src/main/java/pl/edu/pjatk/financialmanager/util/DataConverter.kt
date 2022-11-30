package pl.edu.pjatk.financialmanager.util

import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*

class DataConverter {

    companion object {
        @JvmStatic
        fun formatStringToDecimal(string: String): BigDecimal {
            return string.replace("[ zł,.]".toRegex(), "")
                .toBigDecimal()
                .divide(BigDecimal(100))
        }

        @JvmStatic
        fun isZero(string: String): Boolean =
            string.replace("[ zł,.]".toRegex(), "")
                .toBigDecimal() == BigDecimal.ZERO


        @JvmStatic
        fun formatToAmount(string: String): String {
            return NumberFormat
                .getCurrencyInstance(Locale("pl", "PL"))
                .format(
                    string.replace("[ zł,.]".toRegex(), "")
                        .toBigDecimal()
                        .divide(BigDecimal(100))
                )
        }

        @JvmStatic
        fun getZero() = NumberFormat
            .getCurrencyInstance(Locale("pl", "PL"))
            .format(BigDecimal.ZERO)
    }
}