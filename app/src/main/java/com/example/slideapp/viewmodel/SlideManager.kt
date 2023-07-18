package com.example.slideapp.viewmodel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.slideapp.model.SlideSquareView

class SlideManager : ViewModel() {


    private val _slideSquareList = MutableLiveData<List<SlideSquareView>>()

    val slideSquareList: LiveData<List<SlideSquareView>> get() = _slideSquareList

    private val _slideSquareView = MutableLiveData<SlideSquareView>()
    val slideSquareView: LiveData<SlideSquareView> get() = _slideSquareView

    private val _viewTouch = MutableLiveData<Boolean>()
    val viewTouch: LiveData<Boolean> get() = _viewTouch

    fun getSlideSquareView(index: Int) {
        val slideSquareListValue = slideSquareList.value
        if (slideSquareListValue != null && index >= 0 && index < slideSquareListValue.size) {
            val selectedSlideSquareView = slideSquareListValue[index]
            _slideSquareView.value = selectedSlideSquareView
        }
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