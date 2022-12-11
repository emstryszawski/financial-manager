package pl.edu.pjatk.financialmanager

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import pl.edu.pjatk.financialmanager.databinding.ActivityMonthlyChartBinding
import pl.edu.pjatk.financialmanager.viewmodel.TransactionViewModel
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month

class MonthlyChartActivity : AppCompatActivity() {
    lateinit var binding: ActivityMonthlyChartBinding
    lateinit var transactionViewModel: TransactionViewModel

    lateinit var surfaceView: SurfaceView
    lateinit var surfaceHolder: SurfaceHolder

    private val currentMonth = LocalDateTime.now().monthValue
    private val currentYear = LocalDateTime.now().year

    private var startDate = LocalDateTime.of(currentYear, currentMonth, 1, 0, 0, 0)
    private var endDate =
        LocalDateTime.of(currentYear, currentMonth, Month.of(currentMonth).maxLength(), 0, 0, 0)

    private val firstYear = 2020

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMonthlyChartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        transactionViewModel = ViewModelProvider(
            this,
            TransactionViewModel.Factory
        )[TransactionViewModel::class.java]

        binding.monthsSpinner.setSelection(currentMonth - 1)
        binding.yearsSpinner.setSelection(currentYear - firstYear)

        surfaceView = binding.surfaceView
        surfaceHolder = surfaceView.holder

        surfaceView.post {
            drawChart()
        }

        binding.monthsSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                surfaceView.post {
                    drawChart()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun drawOnCanvas() {
        var paint = Paint()
        paint.color = Color.GREEN
        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 8f

        val transactions = getPathFromTransactions()
        val canvas: Canvas = surfaceHolder.lockCanvas()
        val listOfPaths = ArrayList<Pair<Path, Paint>>()
        var startX = 10f
        val startY = canvas.height / 2
        var lastX = 0f
        var lastY = canvas.height / 2f
        for (i in 1..transactions.size) {
            val path = Path()
            val balance = transactions[i]!!.toFloat()
            startX += 100f
            path.moveTo(lastX, lastY)
            path.lineTo(startX, startY + balance / 10)
            lastX = startX
            lastY = startY + balance / 10
            if (transactions[i]!!.toFloat() < 0) {
                paint = Paint()
                paint.color = Color.RED
                paint.isAntiAlias = true
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = 8f
                listOfPaths.add(Pair(path, paint))
            } else {
                paint = Paint()
                paint.color = Color.GREEN
                paint.isAntiAlias = true
                paint.style = Paint.Style.STROKE
                paint.strokeWidth = 8f
                listOfPaths.add(Pair(path, paint))
            }
        }
        canvas.drawColor(Color.WHITE)
        listOfPaths.forEach {
            canvas.drawPath(it.first, it.second)
        }
        surfaceHolder.unlockCanvasAndPost(canvas)
    }

    private fun drawChart() {
        val month = getMonthFromSpinner()
        val year = getYearFromSpinner()

        startDate = getStartDate(year, month)
        endDate = getEndDate(year, month)

        drawOnCanvas()
    }

    private fun getMonthFromSpinner() = binding.monthsSpinner.selectedItemPosition + 1

    private fun getYearFromSpinner() = binding.yearsSpinner.selectedItem.toString().toInt()

    private fun getStartDate(year: Int, month: Int): LocalDateTime =
        LocalDateTime.of(year, month, 1, 0, 0, 0)

    private fun getEndDate(year: Int, month: Int): LocalDateTime =
        LocalDateTime.of(year, month, Month.of(month).maxLength(), 23, 59, 59)

    private fun getPathFromTransactions(): HashMap<Int, BigDecimal> {
        val numberOfDays =
            if (currentMonth == getMonthFromSpinner()) LocalDate.now().dayOfMonth
            else Month.of(getMonthFromSpinner()).maxLength()

        val balancePerDayMap: HashMap<Int, BigDecimal> = HashMap()
        var startingBalance = transactionViewModel.getBalanceToDate(this, startDate)
        balancePerDayMap[1] = startingBalance
        val transactionList = transactionViewModel.getTransactionsInInterval(
            startDate,
            startDate.plusDays(numberOfDays.toLong())
        )
        for (day in 2..numberOfDays) {
            transactionList.forEach { transaction ->
                if (transaction.dateOfTransaction.dayOfMonth == day) {
                    startingBalance += transaction.amount
                }
            }
            balancePerDayMap[day] = startingBalance
        }
        return balancePerDayMap
    }
}