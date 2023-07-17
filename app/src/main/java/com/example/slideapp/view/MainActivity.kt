package com.example.slideapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.slideapp.R
import com.example.slideapp.model.SlideSquareView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val react1 = SlideSquareView.createRandomSlideSquareView()
        Log.d("로그", react1.toString())

        val react2 = SlideSquareView.createRandomSlideSquareView()
        Log.d("로그", react2.toString())

        val react3 = SlideSquareView.createRandomSlideSquareView()
        Log.d("로그", react3.toString())

        val react4 = SlideSquareView.createRandomSlideSquareView()
        Log.d("로그", react4.toString())

    }
}