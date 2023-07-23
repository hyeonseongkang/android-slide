package com.example.slideapp.models

import java.util.UUID
import kotlin.random.Random

class SlideSquareView private constructor(
    var index: Int,
    val id: String,
    var isSquare: Boolean,
    var alpha: Int,
    var square: Square,
    var photo: Photo? = null

) {
    override fun toString(): String {
        return "Rect${index} (${id}), Slide:${square.slide}, R:${square.backgroundColor.r}, G:${square.backgroundColor.g}, B:${square.backgroundColor.b}, Alpha: ${alpha}"
    }

    companion object Factory {
        var index = 0
        fun createRandomSlideSquareView(): SlideSquareView {
            val square = Random.nextInt(2)
            val id = generateUniqueId()
            index++
            val alpha = Random.nextInt(1, 11)
            val slide = Random.nextInt(100, 500)
            val length = Random.nextInt(1, 101)
            val backgroundColor = generateRandomColor()
            return if (square == 0) {
                SlideSquareView(index, id, true, alpha, Square(slide, length, backgroundColor))
            } else {
                SlideSquareView(index, id, false, 10, Square(0,  0,Color(0, 0, 0)), null)
            }
        }

        private fun generateUniqueId(): String {
            val uuid = UUID.randomUUID().toString().replace("-", "")
            val id = StringBuilder()
            for (i in 0 until 3) {
                id.append(uuid.substring(i * 3, (i + 1) * 3))
                if (i < 2) {
                    id.append("-")
                }
            }
            return id.toString()
        }

        private fun generateRandomColor(): Color {
            val red = Random.nextInt(256)
            val green = Random.nextInt(256)
            val blue = Random.nextInt(256)
            return Color(red, green, blue)
        }
    }
}