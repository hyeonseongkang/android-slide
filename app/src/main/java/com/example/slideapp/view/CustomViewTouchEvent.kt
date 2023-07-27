package com.example.slideapp.view

import android.view.MotionEvent
import com.example.slideapp.enums.DrawingType
import com.example.slideapp.models.Point

fun CustomView.handleTouchEvent(event: MotionEvent): Boolean {
    when (event.action) {
        MotionEvent.ACTION_DOWN -> handleActionDown(event)
        MotionEvent.ACTION_MOVE -> handleActionMove(event)
        MotionEvent.ACTION_UP -> handleActionUp()
    }
    invalidate()
    return true
}

private fun CustomView.handleActionDown(event: MotionEvent) {
    when (currentDrawingType) {
        DrawingType.DRAW -> {
            path.reset()
            pointsList.add(Point(event.x, event.y))
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

private fun CustomView.handleActionMove(event: MotionEvent) {
    when (currentDrawingType) {
        DrawingType.DRAW -> {
            path.lineTo(event.x, event.y)
            pointsList.add(Point(event.x, event.y))
        }
        else -> {
        }
    }
}

private fun CustomView.handleActionUp() {
    when (currentDrawingType) {
        DrawingType.DRAW -> {
            drawingListener?.onDrawingComplete(pointsList)
            path.reset()
        }
        else -> {
        }
    }
}