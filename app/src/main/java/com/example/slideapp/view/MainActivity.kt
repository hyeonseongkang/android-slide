package com.example.slideapp.view

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.slideapp.R
import com.example.slideapp.databinding.ActivityMainBinding
import com.example.slideapp.model.SlideSquareView
import com.example.slideapp.viewmodel.SlideManager

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var viewmodel: SlideManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        btnClick()
        observer()

    }

    private fun init() {

        viewmodel = ViewModelProvider(this)[SlideManager::class.java]

        val react1 = SlideSquareView.createRandomSlideSquareView()
        Log.d("로그", react1.toString())

        val react2 = SlideSquareView.createRandomSlideSquareView()
        Log.d("로그", react2.toString())

        val react3 = SlideSquareView.createRandomSlideSquareView()
        Log.d("로그", react3.toString())

        val react4 = SlideSquareView.createRandomSlideSquareView()
        Log.d("로그", react4.toString())
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun btnClick() {
        binding.rootView.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    viewmodel.isViewTouched(binding.squareView, event.x, event.y)
                    true
                }

                else -> false
            }
        }
    }

    private fun observer() {
        viewmodel.viewTouch.observe(this) { it ->

            when (it) {
                true -> {
                    binding.squareView.setBackgroundResource(R.drawable.yellow_square)
                    binding.viewPropertyModification.visibility = View.VISIBLE
                }

                false -> {
                    binding.squareView.setBackgroundColor(
                        ContextCompat.getColor(
                            this,
                            R.color.yellow
                        )
                    )
                    binding.viewPropertyModification.visibility = View.GONE
                }

            }
        }
    }


}