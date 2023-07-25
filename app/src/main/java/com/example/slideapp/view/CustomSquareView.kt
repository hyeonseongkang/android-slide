package com.example.slideapp.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.core.content.ContextCompat
import com.example.slideapp.R
import com.example.slideapp.listener.doubleTapListener
import com.example.slideapp.listener.singleTapListener

class CustomSquareView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private var backgroundColor: Int = Color.TRANSPARENT
    private var borderColor: Int = Color.TRANSPARENT

    private var imageBitmap: Bitmap? = null
    private var imageRect: Rect? = null

    private var doubleTapListener: doubleTapListener? = null
    private var singleTapListener: singleTapListener? = null

    private var doubleTapStartTime: Long = 0
    private val doubleTapTimeout: Long = ViewConfiguration.getDoubleTapTimeout().toLong()

    private var isSingleTap = false
    private val singleTapTimeout: Long = ViewConfiguration.getDoubleTapTimeout().toLong()

    fun setOnDoubleTapListener(listener: doubleTapListener) {
        doubleTapListener = listener
    }

    fun setOnSingleTapListener(listener: singleTapListener) {
        singleTapListener = listener
    }

    private fun onSingleTap() {
        singleTapListener?.onSingleTap()
    }

    private fun onDoubleTap() {
        doubleTapListener?.onDoubleTap()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                val currentTime = System.currentTimeMillis()
                if (currentTime - doubleTapStartTime <= doubleTapTimeout) {
                    onDoubleTap()
                    removeCallbacks(singleTapRunnable)
                } else {
                    isSingleTap = true
                    postDelayed(singleTapRunnable, singleTapTimeout)
                }
                doubleTapStartTime = currentTime
            }
        }
        return true
    }

    private val singleTapRunnable = Runnable {
        if (isSingleTap) {
            onSingleTap()
        }
        isSingleTap = false
    }

    private fun calculateImageRect() {
        imageBitmap?.let {
            val viewRatio = width.toFloat() / height
            val imageRatio = it.width.toFloat() / it.height

            val left: Int
            val top: Int
            val right: Int
            val bottom: Int

            if (viewRatio > imageRatio) {
                val scaledHeight = height
                val scaledWidth = (scaledHeight * imageRatio).toInt()
                left = (width - scaledWidth) / 2
                top = 0
                right = left + scaledWidth
                bottom = top + scaledHeight
            } else {
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

    private val imagePaint = Paint().apply {
        alpha = 255
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        imageBitmap?.let { bitmap ->
            imageRect?.let { rect ->

                val halfStrokeWidth = borderPaint.strokeWidth / 2

                val imageRectWithBorder = Rect(
                    (rect.left + halfStrokeWidth).toInt(),
                    (rect.top + halfStrokeWidth).toInt(),
                    (rect.right - halfStrokeWidth).toInt(),
                    (rect.bottom - halfStrokeWidth).toInt()
                )
                canvas.drawBitmap(bitmap, null, imageRectWithBorder, imagePaint)

                canvas.drawRect(imageRectWithBorder, borderPaint)
            }
        }

        if (imageBitmap == null) {
            canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), backgroundPaint)

            val halfStrokeWidth = borderPaint.strokeWidth / 2
            canvas.drawRect(
                halfStrokeWidth, halfStrokeWidth,
                width.toFloat() - halfStrokeWidth, height.toFloat() - halfStrokeWidth,
                borderPaint
            )
        }
    }

    fun setColors(backgroundColorString: String) {
        imageBitmap = null
        backgroundColor = Color.parseColor(backgroundColorString)
        backgroundPaint.color = backgroundColor

        val layoutParams = layoutParams
        layoutParams.width = dpToPx(300f).toInt()
        layoutParams.height = dpToPx(300f).toInt()
        setLayoutParams(layoutParams)

        invalidate()
    }

    fun setImage(bitmap: Bitmap, alpha: Int) {
        imageBitmap = bitmap
        imagePaint.alpha = alpha
        calculateImageRect()
        invalidate()
    }

    fun unSelectedView() {
        val whiteColor = ContextCompat.getColor(context, R.color.white)
        borderPaint.color = whiteColor
        invalidate()
    }

    fun selectedView() {
        val faceBookColor = ContextCompat.getColor(context, R.color.face_book)
        borderPaint.color = faceBookColor
        invalidate()
    }

    private fun dpToPx(dp: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics
        )
    }
}