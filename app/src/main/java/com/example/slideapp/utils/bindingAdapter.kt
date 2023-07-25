package com.example.slideapp.utils

import android.util.Log
import android.view.View
import androidx.databinding.BindingAdapter
import com.example.slideapp.R

@BindingAdapter("backgroundColorBinding")
fun backgroundColorBinding(view: View, color: String) {
    if (color=="#000000") {
        view.setBackgroundResource(R.drawable.ic_gallery)
    } else {
        view.setBackgroundColor(parseColor(color))
    }
}