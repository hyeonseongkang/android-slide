package com.example.slideapp.viewmodels

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.slideapp.models.SlideSquareView

class SlideManagerViewModel : ViewModel() {

    private val _slideSquareList = MutableLiveData<List<SlideSquareView>>()
    val slideSquareList: LiveData<List<SlideSquareView>> = _slideSquareList

    private val _slideSquareViewCnt = MutableLiveData<Int>()
    val slideSquareViewCnt: LiveData<Int> = _slideSquareViewCnt

    private val _slideSquareView = MutableLiveData<SlideSquareView>()
    val slideSquareView: LiveData<SlideSquareView> = _slideSquareView

    private val _viewTouch = MutableLiveData<Boolean>()
    val viewTouch: LiveData<Boolean> = _viewTouch

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
    }

    fun getTotalCntSlideSquareView() {
        _slideSquareViewCnt.value = _slideSquareList.value?.size
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