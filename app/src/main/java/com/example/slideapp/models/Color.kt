package com.example.slideapp.models

class Color(val r: Int, val g: Int, val b: Int) {
    fun toColorString(): String {
        val hexR = r.toString(16).padStart(2, '0')
        val hexG = g.toString(16).padStart(2, '0')
        val hexB = b.toString(16).padStart(2, '0')
        val hex = "#$hexR$hexG$hexB".toUpperCase()
        return hex
    }
}