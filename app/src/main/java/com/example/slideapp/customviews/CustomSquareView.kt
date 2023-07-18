package com.example.slideapp.customviews

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.core.content.ContextCompat
import com.example.slideapp.R

class CustomSquareView (context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private var backgroundColor: Int = ContextCompat.getColor(context, R.color.yellow)
    private var borderColor: Int = ContextCompat.getColor(context, R.color.yellow)

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

        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), backgroundPaint)

        val halfStrokeWidth = borderPaint.strokeWidth / 2
        canvas.drawRect(
            halfStrokeWidth, halfStrokeWidth,
            width.toFloat() - halfStrokeWidth, height.toFloat() - halfStrokeWidth,
            borderPaint
        )
    }

    fun setColors(backgroundColorRes: Int, borderColorRes: Int) {
        this.backgroundColor = ContextCompat.getColor(context, backgroundColorRes)
        this.borderColor = ContextCompat.getColor(context, borderColorRes)
        //         this.borderColor = Color.parseColor("#80FF0000") // 반투명한 빨간색 (50% 투명도)
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