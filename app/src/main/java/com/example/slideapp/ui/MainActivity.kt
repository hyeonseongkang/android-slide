package com.example.slideapp.ui

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModelProvider
import com.example.slideapp.view.CustomSquareView
import com.example.slideapp.R
import com.example.slideapp.databinding.ActivityMainBinding
import com.example.slideapp.models.Color
import com.example.slideapp.models.SlideSquareView
import com.example.slideapp.viewmodels.SlideManagerViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var viewmodel: SlideManagerViewModel

    private lateinit var customSquareView: CustomSquareView

    private lateinit var backgroundColor: Color
    private lateinit var combinedColor: String
    private var alpha: Int = 0
    private val borderColor: String = "#1877FE"

    private val alphaValues = arrayOf(
        "00", "1A", "33", "4D", "66", "80", "99", "B3", "CC", "E6", "FF"
    )

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
        viewmodel.setSlideSquareView()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun btnClick() {
        binding.rootView.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    viewmodel.isViewTouched(
                        binding.squareView,
                        binding.centerView,
                        event.x,
                        event.y
                    )
                    true
                }

                else -> false
            }
        }

        binding.btnBackgroundColor.setOnClickListener {
            viewmodel.randomBackgroundColor()
        }

        binding.btnAlphaMinus.setOnClickListener {
            viewmodel.alphaMinus(alpha)
        }

        binding.btnAlphaPlus.setOnClickListener {
            viewmodel.alphaPlus(alpha)
        }
    }

    private fun observer() {
        viewmodel.viewTouch.observe(this) { it ->
            when (it) {
                true -> {
                    this.combinedColor =
                        "#${alphaValues[alpha]}${backgroundColor.toColorString().substring(1)}"
                    customSquareView.setColors(combinedColor, borderColor)
                    binding.viewPropertyModification.visibility = View.VISIBLE
                }

                false -> {
                    this.combinedColor =
                        "#${alphaValues[alpha]}${backgroundColor.toColorString().substring(1)}"
                    customSquareView.setColors(combinedColor, combinedColor)
                    binding.viewPropertyModification.visibility = View.GONE
                }
            }
        }

        viewmodel.backgroundColor.observe(this) { it ->
            this.combinedColor =
                "#${alphaValues[alpha]}${it.toColorString().substring(1)}"
            binding.btnBackgroundColor.setCardBackgroundColor(
                android.graphics.Color.parseColor(
                    combinedColor
                )
            )
            customSquareView.setColors(it.toColorString(), borderColor)
            this.backgroundColor = Color(it.r, it.g, it.b)
            binding.tvBackgroundColorTxt.text = it.toHexString()
        }

        viewmodel.alphaValue.observe(this) { it ->
            this.alpha = it
            binding.tvAlphaTxt.text = alpha.toString()
            this.combinedColor =
                "#${alphaValues[alpha]}${backgroundColor.toColorString().substring(1)}"
            customSquareView.setColors(combinedColor, borderColor)
            binding.btnBackgroundColor.setCardBackgroundColor(
                android.graphics.Color.parseColor(
                    combinedColor
                )
            )
        }

        viewmodel.slideSquareList.observe(this) {
            val slideSquareView = it.last()
            customSquareView = CustomSquareView(this)
            backgroundColor = slideSquareView.backgroundColor
            this.alpha = slideSquareView.alpha

            this.combinedColor =
                "#${alphaValues[alpha]}${backgroundColor.toColorString().substring(1)}"
            customSquareView.setColors(
                combinedColor,
                combinedColor
            )

            val color =
                android.graphics.Color.parseColor(combinedColor)
            binding.btnBackgroundColor.setCardBackgroundColor(color)

            binding.tvBackgroundColorTxt.text = combinedColor
            binding.tvAlphaTxt.text = alpha.toString()
            binding.squareView.addView(customSquareView)
        }
    }
}