package com.example.slideapp.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.slideapp.R
import com.example.slideapp.callback.ItemTouchHelperCallback
import com.example.slideapp.view.CustomView
import com.example.slideapp.adapter.SlideViewAdapter
import com.example.slideapp.databinding.ActivityMainBinding
import com.example.slideapp.enums.DrawingType
import com.example.slideapp.factory.SlideViewModelFactory
import com.example.slideapp.listener.ItemClickListener
import com.example.slideapp.listener.ItemLongClickListener
import com.example.slideapp.listener.DoubleTapListener
import com.example.slideapp.listener.DrawingCompleteListener
import com.example.slideapp.listener.SingleTapListener
import com.example.slideapp.models.Color
import com.example.slideapp.models.Draw
import com.example.slideapp.models.Photo
import com.example.slideapp.models.Point
import com.example.slideapp.models.SlideView
import com.example.slideapp.repository.SlideViewRepository
import com.example.slideapp.utils.combineColor
import com.example.slideapp.utils.convertAlphaStringToValue
import com.example.slideapp.utils.parseColor
import com.example.slideapp.viewmodels.SlideViewModel
import java.io.ByteArrayOutputStream

class MainActivity : AppCompatActivity(), SingleTapListener, DoubleTapListener,
    View.OnLongClickListener, DrawingCompleteListener {

    private val READ_EXTERNAL_STORAGE_REQUEST_CODE = 101

    private lateinit var binding: ActivityMainBinding

    private lateinit var viewmodel: SlideViewModel

    private lateinit var customView: CustomView

    private lateinit var slideViewAdapter: SlideViewAdapter

    private lateinit var backgroundColor: Color
    private lateinit var combinedColor: String
    private var alpha: Int = 0
    private lateinit var slideViewList: List<SlideView>
    private lateinit var selectedSlideView: SlideView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        btnClick()
        observer()
    }

    private fun init() {
        val slideRepository = SlideViewRepository()
        val viewModelFactory = SlideViewModelFactory(slideRepository, SavedStateHandle())

        viewmodel = ViewModelProvider(this, viewModelFactory).get(SlideViewModel::class.java)

        customView = CustomView(this)
        slideViewAdapter = SlideViewAdapter()
        binding.rvSlideList.adapter = slideViewAdapter

        val itemAnimator = DefaultItemAnimator()
        itemAnimator.supportsChangeAnimations = false
        binding.rvSlideList.itemAnimator = itemAnimator

        binding.rvSlideList.layoutManager = LinearLayoutManager(this)

        val itemTouchHelperCallback = ItemTouchHelperCallback(slideViewAdapter)
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.rvSlideList)

        binding.squareView.setOnSingleTapListener(this)
        binding.squareView.setOnDoubleTapListener(this)
        binding.squareView.setDrawingCompleteListener(this)

        viewmodel.loadSlideManagerState()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun btnClick() {
        binding.centerView.setOnClickListener {
            binding.squareView.unSelectedView()
        }

        binding.btnBackgroundColor.setOnClickListener {
            if (selectedSlideView.type == "Image")
                return@setOnClickListener
            viewmodel.randomBackgroundColor()
        }

        binding.btnAlphaMinus.setOnClickListener {
            viewmodel.alphaMinus(alpha)
        }

        binding.btnAlphaPlus.setOnClickListener {
            viewmodel.alphaPlus(alpha)
        }

        binding.btnAddSlide.setOnClickListener {
            viewmodel.setSlideSquareView()
        }

        slideViewAdapter.setItemClickListener(object : ItemClickListener {
            override fun onClick(v: View, position: Int) {
                selectedSlideView = slideViewList[position]
                backgroundColor = selectedSlideView.square.backgroundColor
                alpha = selectedSlideView.alpha
                binding.tvAlphaTxt.text = alpha.toString()
                customView.resetView()
                binding.tvBackgroundColorTxt.text = ""
                binding.btnBackgroundColor.setBackgroundResource(R.color.black)

                when (selectedSlideView.type) {
                    "Square" -> {
                        combinedColor = combineColor(alpha, backgroundColor)

                        binding.squareView.setColors(
                            combinedColor,
                        )
                        binding.btnBackgroundColor.setBackgroundColor(parseColor(backgroundColor.toColorString()))
                        binding.tvBackgroundColorTxt.text = combinedColor

                        binding.squareView.setDrawingType(DrawingType.SQUARE)
                    }

                    "Image" -> {
                        binding.squareView.setDrawingType(DrawingType.IMAGE)
                        selectedSlideView.photo?.toBitmap()?.let {
                            customView.setImage(
                                it,
                                convertAlphaStringToValue(alpha)
                            )
                        }
                    }

                    "Draw" -> {
                        binding.squareView.setDrawingType(DrawingType.DRAW)
                        selectedSlideView.draw?.let {
                            customView.setPoint(it.path, combineColor(10, backgroundColor))
                        }
                    }
                }
                binding.squareView.selectedView()
            }
        })

        slideViewAdapter.setItemLongClickListener(object : ItemLongClickListener {
            override fun onLongClick(v: View, position: Int) {
                val popupMenu = PopupMenu(v.context, v)
                popupMenu.inflate(R.menu.menu)

                popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
                    when (menuItem.itemId) {
                        R.id.action_send_to_back -> {
                            slideViewAdapter.onItemMove(position, slideViewList.size - 1)
                            true
                        }

                        R.id.action_send_backward -> {
                            slideViewAdapter.onItemMove(position, position + 1)
                            true
                        }

                        R.id.action_send_forward -> {
                            slideViewAdapter.onItemMove(position, position - 1)
                            true
                        }

                        R.id.action_send_to_front -> {
                            slideViewAdapter.onItemMove(position, 0)
                            true
                        }

                        else -> false
                    }
                }
                popupMenu.show()
            }
        })

        binding.btnAddSlide.setOnLongClickListener(this)

        customView = binding.squareView

    }

    override fun onLongClick(v: View): Boolean {
        viewmodel.getSlidesData()
        return true
    }

    override fun onDoubleTap() {
        if (selectedSlideView.type == "Image")
            openGallery()
    }

    override fun onDrawingComplete(path: List<Point>) {
        selectedSlideView.draw = Draw(path)
    }

    override fun onSingleTap() {
        if ((selectedSlideView.type == "Square")) {
            combinedColor = combineColor(alpha, backgroundColor)
            binding.squareView.setColors(combinedColor)
        } else {
            selectedSlideView.photo?.toBitmap()
                ?.let { binding.squareView.setImage(it, convertAlphaStringToValue(alpha)) }
        }
        binding.squareView.selectedView()
    }


    @SuppressLint("ResourceAsColor")
    private fun observer() {
        viewmodel.backgroundColor.observe(this) { it ->
            binding.btnBackgroundColor.setBackgroundColor(parseColor(it.toColorString()))

            backgroundColor = Color(it.R, it.G, it.B)
            selectedSlideView.square.backgroundColor = Color(it.R, it.G, it.B)
            combinedColor = combineColor(alpha, backgroundColor)
            when (selectedSlideView.type) {
                "Square" -> {

                    binding.squareView.setColors(
                        combinedColor
                    )
                }

                "Draw" -> {
                    binding.squareView.setLineColor(combineColor(10, backgroundColor))
                }
            }


            binding.tvBackgroundColorTxt.text = combinedColor

            binding.squareView.selectedView()
        }

        viewmodel.alphaValue.observe(this) { it ->
            alpha = it
            binding.tvAlphaTxt.text = alpha.toString()
            selectedSlideView.alpha = alpha

            if ((selectedSlideView.type == "Square")) {
                combinedColor = combineColor(alpha, backgroundColor)
                binding.squareView.setColors(combinedColor)
                binding.tvBackgroundColorTxt.text = combinedColor
            } else {
                selectedSlideView.photo?.toBitmap()?.let { it1 ->
                    binding.squareView.setImage(
                        it1,
                        convertAlphaStringToValue(alpha)
                    )
                }
                binding.tvBackgroundColorTxt.text = ""
            }

            binding.squareView.selectedView()

        }

        viewmodel.slideManager.observe(this) {
            it?.let {
                val slideList = it.slideList
                viewmodel.saveSlideManagerState()
                slideViewAdapter.setSlideViewList(slideList.last())
                binding.slideView = slideList.last()

                slideViewList = slideList
                selectedSlideView = slideList.last()

                alpha = selectedSlideView.alpha
                binding.tvAlphaTxt.text = alpha.toString()
                binding.tvBackgroundColorTxt.text = ""
                binding.btnBackgroundColor.setBackgroundResource(R.color.black)
                backgroundColor = selectedSlideView.square.backgroundColor
                binding.squareView.resetView()

                when (selectedSlideView.type) {
                    "Square" -> {
                        combinedColor = combineColor(alpha, backgroundColor)

                        binding.squareView.setColors(
                            combinedColor,
                        )
                        binding.btnBackgroundColor.setBackgroundColor(parseColor(backgroundColor.toColorString()))
                        binding.tvBackgroundColorTxt.text = combinedColor

                        binding.squareView.setDrawingType(DrawingType.SQUARE)
                    }

                    "Image" -> {
                        binding.squareView.setDrawingType(DrawingType.IMAGE)

                        selectedSlideView.photo?.toBitmap()?.let {
                            customView.setImage(
                                it,
                                convertAlphaStringToValue(alpha)
                            )
                        }
                    }

                    "Draw" -> {
                        selectedSlideView.draw?.let {
                            customView.setPoint(it.path, combineColor(10, backgroundColor))
                        }
                        binding.squareView.setDrawingType(DrawingType.DRAW)
                    }
                }
            }

        }

        viewmodel.slidesData.observe(this, Observer { slides ->
            for (slide in slides) {
                viewmodel.setSlideView(slide)
            }
        })
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, READ_EXTERNAL_STORAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == READ_EXTERNAL_STORAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val imageUri = data?.data
            if (imageUri != null) {
                try {
                    val inputStream = contentResolver.openInputStream(imageUri)
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    inputStream?.close()

                    if (bitmap != null) {
                        val byteArrayOutputStream = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
                        val byteArray = byteArrayOutputStream.toByteArray()
                        val photo = Photo(byteArray)
                        selectedSlideView.photo = photo
                        customView.setImage(bitmap, convertAlphaStringToValue(alpha))
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewmodel.saveSlideManagerState()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewmodel.saveSlideManagerState()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        viewmodel.loadSlideManagerState()
    }
}