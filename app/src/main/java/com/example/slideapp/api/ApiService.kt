package com.example.slideapp.api

import com.example.slideapp.models.Slide
import com.example.slideapp.models.SlideResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiService {
    @GET("jk/softeer-bootcamp/square-only-slides.json")
    suspend fun getSquareSlides(): SlideResponse

    @GET("jk/softeer-bootcamp/image-slides.json")
    suspend fun getImageSlides(): SlideResponse
}