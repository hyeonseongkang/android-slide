package com.example.slideapp.listener

import android.view.View

interface ItemClickListener {
    fun onClick(v: View, position: Int)
}