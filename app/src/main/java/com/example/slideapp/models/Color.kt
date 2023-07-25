package com.example.slideapp.models

data class Color(val R: Int, val G: Int, val B: Int) {
    fun toColorString(): String {
        val hexR = R.toString(16).padStart(2, '0')
        val hexG = G.toString(16).padStart(2, '0')
        val hexB = B.toString(16).padStart(2, '0')
        val hex = "#$hexR$hexG$hexB".toUpperCase()
        return hex
    }
}