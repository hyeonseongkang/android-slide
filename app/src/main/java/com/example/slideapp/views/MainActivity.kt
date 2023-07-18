package com.example.slideapp.views

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.slideapp.customviews.CustomSquareView
import com.example.slideapp.R
import com.example.slideapp.databinding.ActivityMainBinding
import com.example.slideapp.models.SlideSquareView
import com.example.slideapp.viewmodels.SlideManagerViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var viewmodel: SlideManagerViewModel

    private lateinit var customSquareView: CustomSquareView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        btnClick()
        observer()
    }

    private fun init() {

        viewmodel = ViewModelProvider(this)[SlideManagerViewModel::class.java]

        customSquareView = CustomSquareView(this)
        binding.squareView.addView(customSquareView)

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
              //      customView.setColors(R.color.yellow, R.color.face_book)
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
                    customSquareView.setColors(R.color.yellow, R.color.face_book)
                    binding.viewPropertyModification.visibility = View.VISIBLE
                }

                false -> {
                    customSquareView.setColors(R.color.yellow, R.color.yellow)
                    binding.viewPropertyModification.visibility = View.GONE
                }

            }
        }
    }


}