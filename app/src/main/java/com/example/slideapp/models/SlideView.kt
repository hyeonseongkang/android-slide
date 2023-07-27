package com.example.slideapp.models

import java.util.UUID
import kotlin.random.Random

class SlideView private constructor(
    var index: Int,
    val id: String,
    var type: String,
    var alpha: Int,
    var square: Square,
    var draw: Draw? = null,
    var photo: Photo? = null

) {
    override fun toString(): String {
        return "Rect${index} (${id}), R:${square.backgroundColor.R}, G:${square.backgroundColor.G}, B:${square.backgroundColor.B}, Alpha: ${alpha}"
    }

    companion object Factory {
        var index = 0
        fun createRandomSlideSquareView(): SlideView {
            val type = Random.nextInt(3)
            val id = generateUniqueId()
            index++
            val alpha = Random.nextInt(1, 11)
            val slide = Random.nextInt(100, 500)
            val length = Random.nextInt(1, 101)
            val backgroundColor = generateRandomColor()
            val viewType = when (type) {
                0 -> "Square"
                1 -> "Image"
                2 -> "Draw"
                else -> "Square"
            }

            return when (viewType) {
                "Square" -> SlideView(index, id, viewType, alpha, Square(length, backgroundColor))
                "Image" -> SlideView(index, id, viewType, 10, Square(0, Color(0, 0, 0)))
                "Draw" -> SlideView(index, id, viewType, 10, Square(0, Color(0, 0, 0)))
                else -> SlideView(index, id, viewType, alpha, Square(length, backgroundColor))
            }
        }

        fun setSlideView(slide: Slide): SlideView {
            index++
            return if (slide.type == "Square") {
                SlideView(index, slide.id, "Square", slide.alpha, Square(slide.size, Color(slide.color.R, slide.color.G, slide.color.B)))
            } else {
                SlideView(index, slide.id, "Image" , slide.alpha, Square(0, Color(0, 0,0,)), null,
                    slide.image?.let { Photo(it) })
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