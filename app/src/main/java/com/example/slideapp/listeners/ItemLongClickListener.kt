package com.example.slideapp.listeners

import android.view.View

interface ItemLongClickListener {
    fun onLongClick(v: View, position: Int)
}