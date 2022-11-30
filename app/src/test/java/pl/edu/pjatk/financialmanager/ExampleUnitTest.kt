package pl.edu.pjatk.financialmanager

import org.junit.Test

import org.junit.Assert.*
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        var s = "1 000,00 zł"
        val spaceSign = " "
        val cleanString = s.toString().replace("[$spaceSign zł,.]".toRegex(), "")
        println(cleanString.toBigDecimal().divide(BigDecimal(100)))
    }
}