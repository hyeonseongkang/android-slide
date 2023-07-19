package com.example.slideapp.utils

import com.example.slideapp.models.Color

private val alphaValues = arrayOf(
    "00", "1A", "33", "4D", "66", "80", "99", "B3", "CC", "E6", "FF"
)

fun combineColor(alpha: Int, backgroundColor: Color): String {
        return "#${alphaValues[alpha]}${backgroundColor.toColorString().substring(1)}"
}