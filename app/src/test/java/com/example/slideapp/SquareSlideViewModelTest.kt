package com.example.slideapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.slideapp.viewmodels.SlideViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.MockitoAnnotations

class SquareSlideViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()


    private lateinit var viewModel: SlideViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        viewModel = SlideViewModel()

    }

    @Test
    fun testSetSlideSquareView() {
        val initialListSize = viewModel.slideSquareList.value?.size ?: 0

        viewModel.setSlideSquareView()

        val updatedListSize = viewModel.slideSquareList.value?.size ?: 0
        assertEquals(initialListSize + 1, updatedListSize)
    }


    @Test
    fun testGetSlideSquareView() {
        viewModel.setSlideSquareView()
        val lastIndex = (viewModel.slideSquareList.value?.size ?: 0) - 1

        viewModel.getSlideSquareView(lastIndex)

        val selectedSlideSquareView = viewModel.slideView.value
        assertNotNull(selectedSlideSquareView)
    }

    @Test
    fun testAlphaMinus() {
        viewModel.alphaMinus(5)

        var alphaValue = viewModel.alphaValue.value
        assertEquals(4, alphaValue ?: 0)

        viewModel.alphaMinus(0)
        alphaValue = viewModel.alphaValue.value

        assertEquals(0, alphaValue ?: 0)
    }



    @Test
    fun testAlphaPlus() {
        viewModel.alphaPlus(8)

        var alphaValue = viewModel.alphaValue.value
        assertEquals(9, alphaValue ?: 0)

        viewModel.alphaPlus(10)
        alphaValue = viewModel.alphaValue.value

        assertEquals(10, alphaValue ?: 0)
    }

    @Test
    fun testGetTotalCntSlideSquareView() {

        viewModel.setSlideSquareView()
        viewModel.setSlideSquareView()
        viewModel.setSlideSquareView()

        viewModel.getTotalCntSlideSquareView()

        val actualResult = viewModel.slideSquareViewCnt.value
        assertEquals(3, actualResult)
    }

    @Test
    fun testRandomBackgroundColor() {
        val initialBackgroundColor = viewModel.backgroundColor.value

        viewModel.randomBackgroundColor()

        val newBackgroundColor = viewModel.backgroundColor.value
        assertNotNull(newBackgroundColor)
        assertNotEquals(initialBackgroundColor, newBackgroundColor)
    }

}