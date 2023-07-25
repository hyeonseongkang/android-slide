package com.example.slideapp.listener

import android.view.View

interface ItemLongClickListener {
    fun onLongClick(v: View, position: Int)
}