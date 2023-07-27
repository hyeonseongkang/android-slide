package com.example.slideapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.slideapp.manager.SlideManager
import com.example.slideapp.models.Color
import com.example.slideapp.models.Slide
import com.example.slideapp.models.SlideView
import com.example.slideapp.repository.SlideViewRepository
import kotlinx.coroutines.launch
import kotlin.random.Random

class SlideViewModel(
    private val slideRepository: SlideViewRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _slideManager = MutableLiveData<SlideManager>()
    val slideManager: LiveData<SlideManager> = _slideManager

    private val _slideSquareViewCnt = MutableLiveData<Int>()
    val slideSquareViewCnt: LiveData<Int> = _slideSquareViewCnt

    private val _slideView = MutableLiveData<SlideView>()
    val slideView: LiveData<SlideView> = _slideView

    private val _backgroundColor = MutableLiveData<Color>()
    val backgroundColor: LiveData<Color> = _backgroundColor

    private val _alphaValue = MutableLiveData<Int>()
    val alphaValue: LiveData<Int> = _alphaValue

    private val _slidesData = MutableLiveData<List<Slide>>()
    val slidesData: LiveData<List<Slide>> = _slidesData

    init {
        val savedSlideManager: SlideManager? = savedStateHandle["slideManager"]
        _slideManager.value = savedSlideManager ?: SlideManager(emptyList(), 0)
    }

    fun saveSlideManagerState() {
        savedStateHandle["slideManager"] = _slideManager.value
    }

    fun loadSlideManagerState() {
        val savedSlideManager: SlideManager? = savedStateHandle["slideManager"]
        _slideManager.value = savedSlideManager
    }

    fun getSlideSquareView(index: Int) {
        val slideListValue = slideManager.value?.slideList
        if (slideListValue != null && index >= 0 && index < slideListValue.size) {
            val selectedSlideSquareView = slideListValue[index]
            _slideView.value = selectedSlideSquareView
        }
    }

    fun setSlideSquareView() {
        val slideView = SlideView.createRandomSlideSquareView()
        val currentSlideSquareList = _slideManager.value?.slideList?.toMutableList() ?: mutableListOf()
        currentSlideSquareList.add(slideView)
        _slideManager.value = SlideManager(currentSlideSquareList, currentSlideSquareList.size)
    }

    fun setSlideView(slide: Slide) {
        val slideView = SlideView.setSlideView(slide)
        val currentSlideSquareList = _slideManager.value?.slideList?.toMutableList() ?: mutableListOf()
        currentSlideSquareList.add(slideView)
        _slideManager.value = SlideManager(currentSlideSquareList, currentSlideSquareList.size)
    }

    fun getTotalCntSlideSquareView() {
        _slideSquareViewCnt.value = _slideManager.value?.slideList?.size
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

    fun getSlidesData() {
        viewModelScope.launch {
            val slides = slideRepository.getSlides()
            _slidesData.value = slides
        }
    }
}