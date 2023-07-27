package com.example.slideapp.view

import android.graphics.Rect

fun CustomView.calculateImageRect() {
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