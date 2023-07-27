package com.example.slideapp.manager

import android.os.Parcel
import android.os.Parcelable
import com.example.slideapp.models.SlideView

class SlideManager (
    var slideList: List<SlideView>,
    val slideListSize: Int){
}