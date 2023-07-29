package com.example.slideapp.utils

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.slideapp.R
import com.example.slideapp.adapter.SlideViewAdapter
import com.example.slideapp.models.Color
import com.example.slideapp.models.SlideView
import com.example.slideapp.view.CustomView
import com.example.slideapp.viewmodels.SlideViewModel

@BindingAdapter("backgroundColorBinding")
fun backgroundColorBinding(view: View, type: String) {
    when(type) {
        "Square" -> view.setBackgroundResource(R.drawable.ic_file)
        "Image" -> view.setBackgroundResource(R.drawable.ic_gallery)
        "Draw" -> view.setBackgroundResource(R.drawable.ic_draw)
    }
}

@BindingAdapter(value = ["type", "color", "alpha"], requireAll = false)
fun tvBackgroundColorTxtBinding(textView: TextView, type: String?, color: Color?, alpha: Int?) {
    if (type == "Square" && color != null && alpha != null ) {
        textView.text = combineColor(alpha, color)
    }
}

@BindingAdapter(value = ["type", "color"], requireAll = false)
fun btnBackgroundColorBinding(view: View, type: String?, color: Color?) {
    if (type == "Square" && color != null) {
        view.setBackgroundColor(parseColor(color.toColorString()))
    }
}