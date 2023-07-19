package com.example.slideapp.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat
import com.example.slideapp.R

class CustomSquareView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private var backgroundColor: Int = ContextCompat.getColor(context, R.color.white)
    private var borderColor: Int = ContextCompat.getColor(context, R.color.white)

    private val backgroundPaint = Paint().apply {
        style = Paint.Style.FILL
        color = backgroundColor
    }

    private val borderPaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = dpToPx(2f)
        color = borderColor
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawColor(Color.TRANSPARENT)

        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), backgroundPaint)

        val halfStrokeWidth = borderPaint.strokeWidth / 2
        canvas.drawRect(
            halfStrokeWidth, halfStrokeWidth,
            width.toFloat() - halfStrokeWidth, height.toFloat() - halfStrokeWidth,
            borderPaint
        )
    }

    fun setColors(backgroundColorString: String, borderColorString: String) {
        backgroundPaint.color = Color.TRANSPARENT
        this.backgroundColor = Color.parseColor(backgroundColorString)
        this.borderColor = Color.parseColor(borderColorString)
        backgroundPaint.color = backgroundColor
        borderPaint.color = borderColor
        invalidate()
    }

    private fun dpToPx(dp: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics
        )
    }
}