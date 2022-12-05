package pl.edu.pjatk.financialmanager.util

import org.junit.Test
import org.junit.jupiter.api.Assertions
import java.math.BigDecimal

class CurrencyFormatterTest {

    private val space: String = " "

    @Test
    fun toCurrencyFormat() {
        val bigDecimal = BigDecimal("2137.13")
        val bigDecimal2 = BigDecimal("2137")
        val currencyFormat = CurrencyFormatter.toCurrencyFormat(bigDecimal)
        val currencyFormat2 = CurrencyFormatter.toCurrencyFormat(bigDecimal2)
        Assertions.assertEquals("2${space}137,13${space}zł", currencyFormat)
        Assertions.assertEquals("2${space}137,00${space}zł", currencyFormat2)
    }
}