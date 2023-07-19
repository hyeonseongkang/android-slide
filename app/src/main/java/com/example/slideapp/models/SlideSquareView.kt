package com.example.slideapp.models
import java.util.UUID
import kotlin.random.Random

class SlideSquareView private constructor(val index: Int, val id: String, val slide: Int, val length: Int, val alpha: Int, val backgroundColor: Color) {

    override fun toString(): String {
        return "Rect${index} (${id}), Slide:${slide}, R:${backgroundColor.r}, G:${backgroundColor.g}, B:${backgroundColor.b}, Alpha: ${alpha}"
    }
    companion object Factory {
        private var index: Int = 0

        fun createRandomSlideSquareView(): SlideSquareView {
            index++
            val id = generateUniqueId()
            val slide = Random.nextInt(100, 500)
            val length = Random.nextInt(1, 101)
            val backgroundColor = generateRandomColor()
            val alpha = Random.nextInt(1, 11)
            return SlideSquareView(index, id, slide, length, alpha, backgroundColor)
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