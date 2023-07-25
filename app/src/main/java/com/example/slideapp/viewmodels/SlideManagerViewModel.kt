package com.example.slideapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.slideapp.models.Color
import com.example.slideapp.models.SlideSquareView
import kotlin.random.Random

class SlideManagerViewModel : ViewModel() {

    private val _slideSquareList = MutableLiveData<List<SlideSquareView>>()
    val slideSquareList: LiveData<List<SlideSquareView>> = _slideSquareList

    private val _slideSquareViewCnt = MutableLiveData<Int>()
    val slideSquareViewCnt: LiveData<Int> = _slideSquareViewCnt

    private val _slideSquareView = MutableLiveData<SlideSquareView>()
    val slideSquareView: LiveData<SlideSquareView> = _slideSquareView

    private val _backgroundColor = MutableLiveData<Color>()
    val backgroundColor: LiveData<Color> = _backgroundColor

    private val _alphaValue = MutableLiveData<Int>()
    val alphaValue: LiveData<Int> = _alphaValue

    fun getSlideSquareView(index: Int) {
        val slideSquareListValue = slideSquareList.value
        if (slideSquareListValue != null && index >= 0 && index < slideSquareListValue.size) {
            val selectedSlideSquareView = slideSquareListValue[index]
            _slideSquareView.value = selectedSlideSquareView
        }
    }

    fun setSlideSquareView() {
        val slideSquareView = SlideSquareView.createRandomSlideSquareView()
        val currentSlideSquareList = slideSquareList.value?.toMutableList() ?: mutableListOf()
        currentSlideSquareList.add(slideSquareView)
        _slideSquareList.value = currentSlideSquareList
        _slideSquareViewCnt.value = _slideSquareList.value?.size
    }

    fun getTotalCntSlideSquareView() {
        _slideSquareViewCnt.value = _slideSquareList.value?.size
    }

    fun randomBackgroundColor() {
        val red = Random.nextInt(256)
        val green = Random.nextInt(256)
        val blue = Random.nextInt(256)
        val color = Color(red, green, blue)

        _backgroundColor.value = color
    }

    fun alphaMinus(alpha: Int) {
        _alphaValue.value = maxOf(alpha - 1, 0)

    }

    fun alphaPlus(alpha: Int) {
        _alphaValue.value = minOf(alpha + 1, 10)
    }
}