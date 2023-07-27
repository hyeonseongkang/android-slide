package com.example.slideapp.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.slideapp.R
import com.example.slideapp.enum.DrawingType
import com.example.slideapp.listener.DoubleTapListener
import com.example.slideapp.listener.DrawingCompleteListener
import com.example.slideapp.listener.SingleTapListener
import com.example.slideapp.models.Point

class CustomView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private var backgroundColor: Int = Color.TRANSPARENT
    private var borderColor: Int = Color.TRANSPARENT

    private var imageBitmap: Bitmap? = null
    private var imageRect: Rect? = null

    private var doubleTapListener: DoubleTapListener? = null
    private var singleTapListener: SingleTapListener? = null

    private var doubleTapStartTime: Long = 0
    private val doubleTapTimeout: Long = ViewConfiguration.getDoubleTapTimeout().toLong()

    private var isSingleTap = false
    private val singleTapTimeout: Long = ViewConfiguration.getDoubleTapTimeout().toLong()

    private var drawingCompleteListener: DrawingCompleteListener? = null

    var pointsList = mutableListOf<Point>()

    private val path = Path()

    fun setDrawingCompleteListener(listener: DrawingCompleteListener) {
        drawingCompleteListener = listener
    }

    fun setOnDoubleTapListener(listener: DoubleTapListener) {
        doubleTapListener = listener
    }

    fun setOnSingleTapListener(listener: SingleTapListener) {
        singleTapListener = listener
    }

    private fun onSingleTap() {
        singleTapListener?.onSingleTap()
    }

    private fun onDoubleTap() {
        doubleTapListener?.onDoubleTap()
    }

    private var currentDrawingType = DrawingType.SQUARE

    fun setDrawingType(type: DrawingType) {
        currentDrawingType = type
        if (type == DrawingType.DRAW) {
            layoutParams.width = dpToPx(0f).toInt()
            layoutParams.height = dpToPx(0f).toInt()
//            layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
//            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            requestLayout()
        } else {
            layoutParams.width = dpToPx(300f).toInt()
            layoutParams.height = dpToPx(300f).toInt()
            requestLayout()
        }
        invalidate()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                when (currentDrawingType) {
                    DrawingType.DRAW -> {
                        path.reset()
                        val point = Point(event.x, event.y)
                        pointsList.add(point)
                        path.moveTo(event.x, event.y)
                    }

                    else -> {
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
            }

            MotionEvent.ACTION_MOVE -> {
                when (currentDrawingType) {
                    DrawingType.DRAW -> {
                        path.lineTo(event.x, event.y)
                        val point = Point(event.x, event.y)
                        pointsList.add(point)
                    }

                    else -> {}
                }
            }

            MotionEvent.ACTION_UP -> {
                when (currentDrawingType) {
                    DrawingType.DRAW -> {
                        drawingCompleteListener?.onDrawingComplete(pointsList)
                        path.reset()
                    }

                    else -> {}
                }
            }
        }
        invalidate()
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

    private val paint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 5f
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        when (currentDrawingType) {
            DrawingType.IMAGE -> {
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
            }

            DrawingType.SQUARE -> {
                canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), backgroundPaint)

                val halfStrokeWidth = borderPaint.strokeWidth / 2
                canvas.drawRect(
                    halfStrokeWidth, halfStrokeWidth,
                    width.toFloat() - halfStrokeWidth, height.toFloat() - halfStrokeWidth,
                    borderPaint
                )
            }

            DrawingType.DRAW -> {

                if (pointsList.isEmpty()) {
                    canvas.drawPath(path, paint)
                    return
                }

                for (i in 0 until pointsList.size - 1) {
                    val startPoint = pointsList[i]
                    val endPoint = pointsList[i + 1]
                    canvas.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y, paint)
                }

            }
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

    fun setPoint(path: List<Point>) {
        pointsList = path as MutableList<Point>
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

    fun resetView() {
        path.reset()
        pointsList = mutableListOf<Point>()
        imageBitmap = null
        imagePaint.alpha = 255
        imageRect = null

        backgroundColor = Color.WHITE
        borderColor = Color.TRANSPARENT
        backgroundPaint.color = backgroundColor
        borderPaint.color = borderColor

        val layoutParams = layoutParams
        layoutParams.width = dpToPx(300f).toInt()
        layoutParams.height = dpToPx(300f).toInt()
        setLayoutParams(layoutParams)

        invalidate()
    }
}