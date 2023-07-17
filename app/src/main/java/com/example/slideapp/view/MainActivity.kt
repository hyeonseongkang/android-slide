package com.example.slideapp.view

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.example.slideapp.R
import com.example.slideapp.databinding.ActivityMainBinding
import com.example.slideapp.model.SlideSquareView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.rootView.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (isViewTouched(binding.squareView, event.x, event.y)) {
                        binding.squareView.setBackgroundResource(R.drawable.yellow_square)
                        binding.viewPropertyModification.visibility = View.VISIBLE
                        Log.d("로그", "Square is touched")
                    } else if (isViewTouched(binding.centerView, event.x, event.y)){
                        binding.squareView.setBackgroundColor(ContextCompat.getColor(this, R.color.yellow))
                        binding.viewPropertyModification.visibility = View.GONE
                        Log.d("로그", "Background is touched")
                    }
                    true
                }
                else -> false
            }
        }

        val react1 = SlideSquareView.createRandomSlideSquareView()
        Log.d("로그", react1.toString())

        val react2 = SlideSquareView.createRandomSlideSquareView()
        Log.d("로그", react2.toString())

        val react3 = SlideSquareView.createRandomSlideSquareView()
        Log.d("로그", react3.toString())

        val react4 = SlideSquareView.createRandomSlideSquareView()
        Log.d("로그", react4.toString())

    }

    private fun isViewTouched(view: View, touchX: Float, touchY: Float): Boolean {
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        val left = location[0]
        val top = location[1]
        val right = left + view.width
        val bottom = top + view.height
        return touchX >= left && touchX <= right && touchY >= top && touchY <= bottom
    }
}