package com.example.slideapp.models

import android.graphics.Bitmap

data class Slide(
    val type: String = "",
    val id: String = "",
    val url: String = "",
    val alpha: Int = 0,
    val size: Int = 0,
    var image: Bitmap? = null,
    val color: Color = Color(0, 0, 0)
)