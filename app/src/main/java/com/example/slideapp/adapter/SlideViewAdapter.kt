package com.example.slideapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.slideapp.R
import com.example.slideapp.models.SlideSquareView

class SlideViewAdapter(private var slideViewList: List<SlideSquareView> = emptyList(), private var squareView: Boolean = false) : RecyclerView.Adapter<SlideViewAdapter.SlideViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlideViewHolder {
        if (squareView) {
            return SlideViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.adapter_slide_square_item, parent, false))
        } else {
            return SlideViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.adapter_slide_photo_item, parent, false))
        }
    }

    override fun onBindViewHolder(holder: SlideViewHolder, position: Int) {
        holder.bind(slideViewList[position])
    }

    override fun getItemCount(): Int {
        return slideViewList.size
    }

    fun setSlideViewList(slideViewList: List<SlideSquareView>, squareView: Boolean) {
        this.slideViewList = slideViewList
        this.squareView = squareView
        notifyDataSetChanged()
    }


    class SlideViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val slideIndex: TextView = view.findViewById(R.id.tv_slide_index)

        @SuppressLint("SetTextI18n")
        fun bind(slideView: SlideSquareView) {
            slideIndex.text = (slideView.index + 1).toString()
        }
    }
}