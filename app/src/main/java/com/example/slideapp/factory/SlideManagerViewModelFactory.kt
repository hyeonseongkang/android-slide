package com.example.slideapp.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.slideapp.repository.SlideViewRepository
import com.example.slideapp.viewmodels.SlideManagerViewModel

class SlideManagerViewModelFactory (private val slideRepository: SlideViewRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SlideManagerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SlideManagerViewModel(slideRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}