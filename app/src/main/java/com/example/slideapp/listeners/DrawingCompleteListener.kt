package com.example.slideapp.listeners

import com.example.slideapp.models.Point

interface DrawingCompleteListener {
    fun onDrawingComplete(path: List<Point>)
}