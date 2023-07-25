package com.example.slideapp.viewmodels

import android.view.View
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

    private val _viewTouch = MutableLiveData<Boolean>()
    val viewTouch: LiveData<Boolean> = _viewTouch

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

    fun setSlideSquareView(index: Int) {
        val slideSquareView = SlideSquareView.createRandomSlideSquareView(index)
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

    fun isViewTouched(squareView: View, centerView: View, touchX: Float, touchY: Float) {
        val squareViewLocation = IntArray(2)
        squareView.getLocationOnScreen(squareViewLocation)
        val squareViewLeft = squareViewLocation[0]
        val squareViewTop = squareViewLocation[1]
        val squareViewRight = squareViewLeft + squareView.width
        val squareViewBottom = squareViewTop + squareView.height

        val centerViewLocation = IntArray(2)
        centerView.getLocationOnScreen(centerViewLocation)
        val centerViewLeft = centerViewLocation[0]
        val centerViewTop = centerViewLocation[1]
        val centerViewRight = centerViewLeft + centerView.width
        val centerViewBottom = centerViewTop + centerView.height
        if (touchX >= squareViewLeft && touchX <= squareViewRight && touchY >= squareViewTop && touchY <= squareViewBottom) {
            _viewTouch.value = true
        } else if (touchX >= centerViewLeft && touchX <= centerViewRight && touchY >= centerViewTop && touchY <= centerViewBottom) {
            _viewTouch.value = false
        }
    }

}