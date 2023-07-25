package com.example.slideapp.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.slideapp.api.ApiClient
import com.example.slideapp.api.ApiClient.apiService
import com.example.slideapp.models.Color
import com.example.slideapp.models.Slide
import com.example.slideapp.utils.downloadImageFromUrl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL
import kotlin.random.Random

class SlideViewRepository {
    suspend fun getSlides(): List<Slide>? {
        return try {
            val randomType = Random.nextInt(2)

            val response = if (randomType == 0) {
                apiService.getImageSlides()
            } else {
                apiService.getSquareSlides()
            }

            val imageSlides = response.slides.filter { it.type == "Image" }
            imageSlides.forEach { slide ->
                slide.image = downloadImageFromUrl(slide.url)
                Log.d("로그", slide.image.toString() + "여기요!!")
            }

            // 모든 Slide 객체 반환
            response.slides
        } catch (e: Exception) {
            Log.e("SlideViewRepository", "Exception: ${e.message}")
            null
        }
    }
}