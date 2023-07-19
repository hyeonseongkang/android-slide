package com.example.slideapp.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.slideapp.R
import com.example.slideapp.listener.ItemTouchHelperListener
import com.example.slideapp.models.SlideSquareView
import java.util.Collections

class SlideViewAdapter(
    private var slideViewList: MutableList<SlideSquareView> = mutableListOf(),
    private var squareView: Boolean = false
) : RecyclerView.Adapter<SlideViewAdapter.SlideViewHolder>(), ItemTouchHelperListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlideViewHolder {
        val layoutRes =
            if (squareView) R.layout.adapter_slide_square_item else R.layout.adapter_slide_photo_item
        val itemView = LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
        return SlideViewHolder(itemView)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        val toPositionIndex = slideViewList[toPosition].index
        slideViewList[toPosition].index = slideViewList[fromPosition].index
        slideViewList[fromPosition].index = toPositionIndex
        Collections.swap(slideViewList, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
        notifyDataSetChanged()
        return true
    }

    override fun onBindViewHolder(holder: SlideViewHolder, position: Int) {
        holder.bind(slideViewList[position])
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }

    override fun getItemCount(): Int {
        return slideViewList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setSlideViewList(slideViewList: MutableList<SlideSquareView>, squareView: Boolean) {
        this.slideViewList = slideViewList
        this.squareView = squareView
        notifyDataSetChanged()
    }

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    private lateinit var itemClickListener: OnItemClickListener

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    class SlideViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val slideIndex: TextView = view.findViewById(R.id.tv_slide_index)
        private val slideBackground: View = view.findViewById(R.id.v_background)

        fun bind(slideView: SlideSquareView) {
            slideIndex.text = (slideView.index + 1).toString()
            slideBackground.setBackgroundColor(Color.parseColor(slideView.backgroundColor.toColorString()))
        }
    }
}