package com.example.slideapp.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.slideapp.R

class CustomSquareView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    // (1) 리스너 인터페이스
    interface OnDoubleTapListener {
        fun onDoubleTap()
    }

    // (2) 외부에서 리스너 설정
    private var doubleTapListener: OnDoubleTapListener? = null

    fun setOnDoubleTapListener(listener: OnDoubleTapListener) {
        this.doubleTapListener = listener
    }

    // (3) 더블클릭 이벤트 처리
    private val doubleTapGestureDetector: GestureDetector

    init {
        doubleTapGestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onDoubleTap(e: MotionEvent): Boolean {
                doubleTapListener?.onDoubleTap() // 더블클릭 이벤트 발생 시 외부에서 설정한 리스너 호출
                return true
            }
        })
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val result = doubleTapGestureDetector.onTouchEvent(event)
        if (!result) {
            // 더블클릭 이벤트가 아닌 경우 기본 터치 이벤트를 처리합니다.
            return super.onTouchEvent(event)
        }
        return result
    }

    private var backgroundColor: Int = ContextCompat.getColor(context, R.color.white)
    private var borderColor: Int = ContextCompat.getColor(context, R.color.white)

    private var imageBitmap: Bitmap? = null
    private var imageRect: Rect? = null

    private fun calculateImageRect() {
        imageBitmap?.let {
            val viewRatio = width.toFloat() / height
            val imageRatio = it.width.toFloat() / it.height

            val left: Int
            val top: Int
            val right: Int
            val bottom: Int

            if (viewRatio > imageRatio) {
                // 이미지의 높이를 맞추고 가로 방향으로 가운데 정렬
                val scaledHeight = height
                val scaledWidth = (scaledHeight * imageRatio).toInt()
                left = (width - scaledWidth) / 2
                top = 0
                right = left + scaledWidth
                bottom = top + scaledHeight
            } else {
                // 이미지의 너비를 맞추고 세로 방향으로 가운데 정렬
                val scaledWidth = width
                val scaledHeight = (scaledWidth / imageRatio).toInt()
                left = 0
                top = (height - scaledHeight) / 2
                right = left + scaledWidth
                bottom = top + scaledHeight
            }

            imageRect = Rect(left, top, right, bottom)
        }
    }

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

        // 이미지 그리기
        imageBitmap?.let { bitmap ->
            imageRect?.let { rect ->
                canvas.drawBitmap(bitmap, null, rect, null)
            }
        }

        // 배경색 및 테두리 그리기
        if (imageBitmap == null) {
            canvas.drawColor(Color.TRANSPARENT)

            canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), backgroundPaint)

            val halfStrokeWidth = borderPaint.strokeWidth / 2
            canvas.drawRect(
                halfStrokeWidth, halfStrokeWidth,
                width.toFloat() - halfStrokeWidth, height.toFloat() - halfStrokeWidth,
                borderPaint
            )
        }
    }

    fun setColors(backgroundColorString: String, borderColorString: String) {
        backgroundPaint.color = Color.TRANSPARENT
        this.backgroundColor = Color.parseColor(backgroundColorString)
        this.borderColor = Color.parseColor(borderColorString)
        backgroundPaint.color = backgroundColor
        borderPaint.color = borderColor
        invalidate()
    }

    fun setImage(bitmap: Bitmap) {
        imageBitmap = bitmap
        calculateImageRect()
        invalidate()
    }

    private fun dpToPx(dp: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics
        )
    }
}