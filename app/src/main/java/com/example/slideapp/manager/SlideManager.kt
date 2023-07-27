package com.example.slideapp.manager

import android.os.Parcelable
import com.example.slideapp.models.SlideView
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
class SlideManager(
    var slideList: @RawValue List<SlideView>,
    val slideListSize: Int
) : Parcelable