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
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.core.content.ContextCompat
import com.example.slideapp.R
import com.example.slideapp.enums.DrawingType
import com.example.slideapp.listeners.DoubleTapListener
import com.example.slideapp.listeners.DrawingCompleteListener
import com.example.slideapp.listeners.SingleTapListener
import com.example.slideapp.models.Point

class CustomView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private var backgroundColor: Int = Color.TRANSPARENT
    private var borderColor: Int = Color.TRANSPARENT

    var imageBitmap: Bitmap? = null
    var imageRect: Rect? = null

    private var doubleTapListener: DoubleTapListener? = null
    private var singleTapListener: SingleTapListener? = null

    var doubleTapStartTime: Long = 0
    val doubleTapTimeout: Long = ViewConfiguration.getDoubleTapTimeout().toLong()

    var isSingleTap = false
    val singleTapTimeout: Long = ViewConfiguration.getDoubleTapTimeout().toLong()

    var drawingListener: DrawingCompleteListener? = null

    var pointsList = mutableListOf<Point>()

    val path = Path()

    var currentDrawingType = DrawingType.SQUARE

    fun setDrawingCompleteListener(listener: DrawingCompleteListener) {
        drawingListener = listener
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

    fun onDoubleTap() {
        doubleTapListener?.onDoubleTap()
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

    val singleTapRunnable = Runnable {
        if (isSingleTap) {
            onSingleTap()
        }
        isSingleTap = false
    }

    val backgroundPaint = Paint().apply {
        style = Paint.Style.FILL
        color = backgroundColor
    }

    val borderPaint = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = dpToPx(2f)
        color = borderColor
    }

    val imagePaint = Paint().apply {
        alpha = 255
    }

    val paint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 5f
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

    fun setPoint(path: List<Point>, color: String = "#000000") {
        pointsList = path as MutableList<Point>
        paint.color = Color.parseColor(color)
    }

    fun setLineColor(color: String) {
        paint.color = Color.parseColor(color)
        invalidate()
    }

    fun setDrawingType(type: DrawingType) {
        currentDrawingType = type
        if (type == DrawingType.DRAW) {
            layoutParams.width = dpToPx(0f).toInt()
            layoutParams.height = dpToPx(0f).toInt()
            requestLayout()
        } else {
            layoutParams.width = dpToPx(300f).toInt()
            layoutParams.height = dpToPx(300f).toInt()
            requestLayout()
        }
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

    private fun onDrawDrawingType(canvas: Canvas) {
        when (currentDrawingType) {
            DrawingType.IMAGE -> drawImageType(canvas)
            DrawingType.SQUARE -> drawSquareType(canvas)
            DrawingType.DRAW -> drawDrawType(canvas)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        onDrawDrawingType(canvas)
        calculateImageRect()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return handleTouchEvent(event)
    }
}