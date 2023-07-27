package com.example.slideapp.listener

import com.example.slideapp.models.Point

interface DrawingCompleteListener {
    fun onDrawingComplete(path: List<Point>)
}