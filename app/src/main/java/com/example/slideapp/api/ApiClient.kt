package com.example.slideapp.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "https://public.codesquad.kr/"
    private const val IMAGE_BASE_URL = "https://www.softeerbootcamp.com/"

    private val retrofit: Retrofit get() = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val retrofitImage: Retrofit get() = Retrofit.Builder()
        .baseUrl(IMAGE_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    var apiService: ApiService = retrofit.create(ApiService::class.java)

    var imageApiService: ApiService = retrofitImage.create(ApiService::class.java)
}