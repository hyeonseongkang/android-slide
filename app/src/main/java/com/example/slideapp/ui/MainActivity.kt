package com.example.slideapp.ui

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.slideapp.R
import com.example.slideapp.callback.ItemTouchHelperCallback
import com.example.slideapp.view.CustomSquareView
import com.example.slideapp.adapter.SlideViewAdapter
import com.example.slideapp.databinding.ActivityMainBinding
import com.example.slideapp.models.Color
import com.example.slideapp.models.SlideSquareView
import com.example.slideapp.utils.combineColor
import com.example.slideapp.utils.parseColor
import com.example.slideapp.viewmodels.SlideManagerViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var viewmodel: SlideManagerViewModel

    private lateinit var customSquareView: CustomSquareView

    private lateinit var slideViewAdapter: SlideViewAdapter

    private lateinit var backgroundColor: Color
    private lateinit var combinedColor: String
    private var alpha: Int = 0
    private var slideViewCnt: Int = 0
    private val borderColor: String = "#1877FE"
    private lateinit var slideViewList: List<SlideSquareView>
    private lateinit var selectedSlideView: SlideSquareView


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
        slideViewAdapter = SlideViewAdapter()
        binding.rvSlideList.adapter = slideViewAdapter

        val itemAnimator = DefaultItemAnimator()
        itemAnimator.supportsChangeAnimations = false
        binding.rvSlideList.itemAnimator = itemAnimator

        binding.rvSlideList.layoutManager = LinearLayoutManager(this)

        val itemTouchHelperCallback = ItemTouchHelperCallback(slideViewAdapter)
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvSlideList)


    }

    @SuppressLint("ClickableViewAccessibility")
    private fun btnClick() {
//        binding.rootView.setOnTouchListener { _, event ->
//            when (event.action) {
//                MotionEvent.ACTION_DOWN -> {
//                    viewmodel.isViewTouched(
//                        binding.squareView,
//                        binding.centerView,
//                        event.x,
//                        event.y
//                    )
//                    true
//                }
//
//                else -> false
//            }
//        }

        binding.btnBackgroundColor.setOnClickListener {
            viewmodel.randomBackgroundColor()
        }

        binding.btnAlphaMinus.setOnClickListener {
            viewmodel.alphaMinus(alpha)
        }

        binding.btnAlphaPlus.setOnClickListener {
            viewmodel.alphaPlus(alpha)
        }

        binding.btnAddSlide.setOnClickListener {
            viewmodel.setSlideSquareView(slideViewCnt)
        }

        slideViewAdapter.setItemClickListener(object : SlideViewAdapter.OnItemClickListener {
            override fun onClick(v: View, position: Int) {
                selectedSlideView = slideViewList[position]
                backgroundColor = selectedSlideView.backgroundColor

                alpha = selectedSlideView.alpha

                combinedColor = combineColor(alpha, backgroundColor)

                binding.squareView.setColors(
                    combinedColor,
                    combinedColor
                )

                binding.btnBackgroundColor.setBackgroundColor(parseColor(backgroundColor.toColorString()))

                binding.tvBackgroundColorTxt.text = combinedColor
                binding.tvAlphaTxt.text = alpha.toString()

            }
        })

        slideViewAdapter.setItemLongClickListener(object : SlideViewAdapter.OnItemLongClickListener {
            override fun onLongClick(v: View, position: Int) {
                Toast.makeText(this@MainActivity, "메뉴바를 보여주셈.", Toast.LENGTH_SHORT).show()

            }
        })

        // CustomSquareView 바인딩
        customSquareView = binding.squareView

        // 더블클릭 이벤트 리스너 설정
        customSquareView.setOnDoubleTapListener(object : CustomSquareView.OnDoubleTapListener {
            override fun onDoubleTap() {
                // 더블클릭 이벤트 처리를 구현합니다.
                // 이곳에서 원하는 기능을 추가합니다.
                Toast.makeText(this@MainActivity, "이미지 영역을 더블클릭하셨습니다.", Toast.LENGTH_SHORT).show()
                Log.d("MainActivity", "Image Double Clicked!")
            }
        })
    }

    private fun observer() {
        viewmodel.viewTouch.observe(this) { it ->
            when (it) {
                true -> {
                    combinedColor = combineColor(alpha, backgroundColor)
                    binding.squareView.setColors(combinedColor, borderColor)
                    binding.viewPropertyModification.visibility = View.VISIBLE
                }

                false -> {
                    combinedColor = combineColor(alpha, backgroundColor)
                    binding.squareView.setColors(combinedColor, combinedColor)
                    binding.viewPropertyModification.visibility = View.GONE
                }
            }
        }

        viewmodel.backgroundColor.observe(this) { it ->
            binding.btnBackgroundColor.setBackgroundColor(parseColor(it.toColorString()))

            this.backgroundColor = Color(it.r, it.g, it.b)
            this.selectedSlideView.backgroundColor = Color(it.r, it.g, it.b)

            combinedColor = combineColor(alpha, backgroundColor)
            binding.squareView.setColors(
                combinedColor,
                combinedColor
            )

            binding.tvBackgroundColorTxt.text = combinedColor
            slideViewAdapter.setSlideViewList(slideViewList as MutableList<SlideSquareView>, true)
        }

        viewmodel.alphaValue.observe(this) { it ->
            this.alpha = it
            binding.tvAlphaTxt.text = alpha.toString()
            this.selectedSlideView.alpha = alpha
            combinedColor = combineColor(alpha, backgroundColor)
            binding.squareView.setColors(combinedColor, borderColor)
            binding.tvBackgroundColorTxt.text = combinedColor
        }

        viewmodel.slideSquareList.observe(this) {
            slideViewAdapter.setSlideViewList(it as MutableList<SlideSquareView>, true)

            this.slideViewList = it
            this.selectedSlideView = it.last()

            backgroundColor = selectedSlideView.backgroundColor
            this.alpha = selectedSlideView.alpha

            combinedColor = combineColor(alpha, backgroundColor)
            binding.squareView.setColors(
                combinedColor,
                combinedColor
            )

            // 이미지 가져오기
//            val imageBitmap = BitmapFactory.decodeResource(resources, R.drawable.steve)
//
//            binding.squareView.setImage(imageBitmap)

            binding.btnBackgroundColor.setBackgroundColor(parseColor(combinedColor))

            binding.tvBackgroundColorTxt.text = backgroundColor.toColorString()
            binding.tvAlphaTxt.text = alpha.toString()
        }

        viewmodel.slideSquareViewCnt.observe(this) {
            this.slideViewCnt = it
        }
    }
}