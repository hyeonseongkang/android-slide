package com.example.slideapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.slideapp.R
import com.example.slideapp.databinding.AdapterSlideViewItemBinding
import com.example.slideapp.listeners.ItemClickListener
import com.example.slideapp.listeners.ItemLongClickListener
import com.example.slideapp.listeners.ItemTouchHelperListener
import com.example.slideapp.models.SlideView

class SlideViewAdapter(
    private var slideViewList: MutableList<SlideView> = mutableListOf()
) : RecyclerView.Adapter<SlideViewAdapter.SlideViewHolder>(), ItemTouchHelperListener {

    private lateinit var itemClickListener: ItemClickListener
    private lateinit var itemLongClickListener: ItemLongClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlideViewHolder {
        val binding =
            AdapterSlideViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SlideViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SlideViewHolder, position: Int) {
        holder.bind(slideViewList[position])

        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }

        holder.itemView.setOnLongClickListener {
            itemLongClickListener.onLongClick(it, position)
            true
        }
    }

    override fun getItemCount(): Int {
        return slideViewList.size
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        if (fromPosition < 0 || toPosition > itemCount - 1) return false

        val movedItem = slideViewList.removeAt(fromPosition)
        slideViewList.add(toPosition, movedItem)

        val startIndex = 1
        for (i in 0 until slideViewList.size) {
            slideViewList[i].index = startIndex + i
        }

        notifyItemMoved(fromPosition, toPosition)
        notifyItemRangeChanged(0, slideViewList.size)

        return true
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setSlideViewList(slide: SlideView) {
        slideViewList.add(slide)
        notifyItemInserted(slideViewList.lastIndex)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun initSlideViewList() {
        slideViewList.clear()
        notifyDataSetChanged()
    }

    fun setItemLongClickListener(onItemLongClickListener: ItemLongClickListener) {
        itemLongClickListener = onItemLongClickListener
    }

    fun setItemClickListener(onItemClickListener: ItemClickListener) {
        itemClickListener = onItemClickListener
    }

    class SlideViewHolder(private val binding: AdapterSlideViewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(slideView: SlideView) {
            binding.tvSlideIndex.text = ""
            binding.vBackground.setBackgroundResource(R.color.mid_gray2)
            binding.square = slideView
        }
    }
}