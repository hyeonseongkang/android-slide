package com.example.slideapp.view

import android.graphics.Canvas
import android.graphics.Rect

fun CustomView.drawImageType(canvas: Canvas) {
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

fun CustomView.drawSquareType(canvas: Canvas) {
    canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), backgroundPaint)
    val halfStrokeWidth = borderPaint.strokeWidth / 2
    canvas.drawRect(
        halfStrokeWidth, halfStrokeWidth,
        width.toFloat() - halfStrokeWidth, height.toFloat() - halfStrokeWidth,
        borderPaint
    )
}

fun CustomView.drawDrawType(canvas: Canvas) {
    if (pointsList.isNotEmpty()) {
        for (i in 0 until pointsList.size - 1) {
            val startPoint = pointsList[i]
            val endPoint = pointsList[i + 1]
            canvas.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y, paint)
        }
    } else {
        canvas.drawPath(path, paint)
    }
}
