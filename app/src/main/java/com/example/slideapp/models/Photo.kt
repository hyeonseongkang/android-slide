package com.example.slideapp.models

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream

class Photo(
    val photoByteArray: ByteArray
) {
    constructor(bitmap: Bitmap) : this(bitmap.toByteArray())

    fun toBitmap(): Bitmap? {
        return BitmapFactory.decodeByteArray(photoByteArray, 0, photoByteArray.size)
    }
}

private fun Bitmap.toByteArray(): ByteArray {
    val stream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.PNG, 100, stream)
    return stream.toByteArray()
}