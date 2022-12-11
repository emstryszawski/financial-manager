package pl.edu.pjatk.financialmanager.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import androidx.lifecycle.Observer
import java.math.BigDecimal

class ChartView(context: Context, attrs: AttributeSet?) : View(context, attrs) {

    companion object {
        var path = HashMap<Int, BigDecimal>()
        var paint = Paint()
    }

    init {
        paint.color = Color.RED
        paint.strokeWidth = 3f
        paint.style = Paint.Style.STROKE
    }

//    override fun onDraw(canvas: Canvas?) {
//        super.onDraw(canvas)
//        canvas?.let {
//
//            if (path.isEmpty()) return
//
//            path.moveTo(myPath[1]!!.toFloat(), myPath[1]!!.toFloat())
//
//            for (i in 1 until myPath.size) {
//                path.lineTo(myPath[i]!!.toFloat(), myPath[i]!!.toFloat())
//            }
//
//            for (i in path.entries) {
//                canvas.drawPath(i.key, paint)
//            }
//
//
//            invalidate()
//        }
//    }
}