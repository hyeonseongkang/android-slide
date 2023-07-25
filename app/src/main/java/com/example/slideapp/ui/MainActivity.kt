package com.example.slideapp.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.slideapp.R
import com.example.slideapp.callback.ItemTouchHelperCallback
import com.example.slideapp.view.CustomSquareView
import com.example.slideapp.adapter.SlideViewAdapter
import com.example.slideapp.databinding.ActivityMainBinding
import com.example.slideapp.listener.ItemClickListener
import com.example.slideapp.listener.ItemLongClickListener
import com.example.slideapp.listener.doubleTapListener
import com.example.slideapp.listener.singleTapListener
import com.example.slideapp.models.Color
import com.example.slideapp.models.Photo
import com.example.slideapp.models.SlideSquareView
import com.example.slideapp.utils.combineColor
import com.example.slideapp.utils.convertAlphaStringToValue
import com.example.slideapp.utils.parseColor
import com.example.slideapp.viewmodels.SlideManagerViewModel
import java.io.ByteArrayOutputStream

class MainActivity : AppCompatActivity(), singleTapListener, doubleTapListener {

    private val READ_EXTERNAL_STORAGE_REQUEST_CODE = 101

    private lateinit var binding: ActivityMainBinding

    private lateinit var viewmodel: SlideManagerViewModel

    private lateinit var customSquareView: CustomSquareView

    private lateinit var slideViewAdapter: SlideViewAdapter

    private lateinit var backgroundColor: Color
    private lateinit var combinedColor: String
    private var alpha: Int = 0
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

        binding.squareView.setOnSingleTapListener(this)
        binding.squareView.setOnDoubleTapListener(this)

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun btnClick() {
        binding.centerView.setOnClickListener {
            binding.squareView.unSelectedView()
        }

        binding.btnBackgroundColor.setOnClickListener {
            if (!selectedSlideView.isSquare)
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
                if (selectedSlideView.isSquare) {
                    combinedColor = combineColor(alpha, backgroundColor)

                    binding.squareView.setColors(
                        combinedColor,
                    )

                    binding.btnBackgroundColor.setBackgroundColor(parseColor(backgroundColor.toColorString()))

                    binding.tvBackgroundColorTxt.text = combinedColor
                    binding.tvAlphaTxt.text = alpha.toString()
                } else {
                    if (selectedSlideView.photo == null) {
                        openGallery()
                    } else {
                        binding.tvBackgroundColorTxt.text = ""
                        binding.btnBackgroundColor.setBackgroundResource(R.color.black)
                        selectedSlideView.photo!!.toBitmap()?.let {
                            customSquareView.setImage(
                                it,
                                convertAlphaStringToValue(alpha)
                            )
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

        customSquareView = binding.squareView
    }

    override fun onDoubleTap() {
        if (selectedSlideView.isSquare)
            return
        openGallery()
    }

    override fun onSingleTap() {
        if (selectedSlideView.isSquare) {
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

            backgroundColor = Color(it.r, it.g, it.b)
            selectedSlideView.square.backgroundColor = Color(it.r, it.g, it.b)

            combinedColor = combineColor(alpha, backgroundColor)
            binding.squareView.setColors(
                combinedColor
            )

            binding.tvBackgroundColorTxt.text = combinedColor
            slideViewAdapter.setSlideViewList(slideViewList as MutableList<SlideSquareView>)

            binding.squareView.selectedView()
        }

        viewmodel.alphaValue.observe(this) { it ->
            alpha = it
            binding.tvAlphaTxt.text = alpha.toString()
            selectedSlideView.alpha = alpha

            if (selectedSlideView.isSquare) {
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

        viewmodel.slideSquareList.observe(this) {
            slideViewAdapter.setSlideViewList(it as MutableList<SlideSquareView>)

            slideViewList = it
            selectedSlideView = it.last()

            alpha = selectedSlideView.alpha
            binding.tvAlphaTxt.text = alpha.toString()
            if (selectedSlideView.isSquare) {
                backgroundColor = selectedSlideView.square.backgroundColor
                combinedColor = combineColor(alpha, backgroundColor)
                binding.squareView.setColors(
                    combinedColor
                )

                binding.btnBackgroundColor.setBackgroundColor(parseColor(combinedColor))

                binding.tvBackgroundColorTxt.text = backgroundColor.toColorString()
            } else {
                binding.btnBackgroundColor.setBackgroundColor(R.color.black)
                binding.tvBackgroundColorTxt.text = ""
                openGallery()
            }
        }
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
                        customSquareView.setImage(bitmap, convertAlphaStringToValue(alpha))
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}