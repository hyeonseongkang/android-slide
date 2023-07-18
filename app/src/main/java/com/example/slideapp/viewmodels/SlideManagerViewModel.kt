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

    fun isViewTouched(view: View, touchX: Float, touchY: Float) {
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        val left = location[0]
        val top = location[1]
        val right = left + view.width
        val bottom = top + view.height
        _viewTouch.value =  touchX >= left && touchX <= right && touchY >= top && touchY <= bottom
    }

}