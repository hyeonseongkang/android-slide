package com.example.slideapp.factory

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.slideapp.repository.SlideViewRepository
import com.example.slideapp.viewmodels.SlideViewModel

class SlideViewModelFactory(
    private val slideRepository: SlideViewRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SlideViewModel::class.java)) {
            return SlideViewModel(slideRepository, savedStateHandle) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}