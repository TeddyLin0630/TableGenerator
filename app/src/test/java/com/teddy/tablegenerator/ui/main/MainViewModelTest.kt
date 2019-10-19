package com.teddy.tablegenerator.ui.main

import com.teddy.tablegenerator.consts.EditError
import org.junit.Before
import org.junit.Test

class MainViewModelTest {

    private lateinit var viewModel: MainViewModel

    @Before
    fun init() {
        viewModel = MainViewModel()
    }

    @Test
    fun checkNumberOfRowAndColumn() {
        val limit = 10
        var result = viewModel.checkNumberOfRowAndColumn(1, limit)
        assert(result == EditError.NONE)

        result = viewModel.checkNumberOfRowAndColumn(-1, limit)
        assert(result == EditError.NUMBER_TOO_SMALL)

        result = viewModel.checkNumberOfRowAndColumn(99, limit)
        assert(result == EditError.NUMBER_TOO_LARGE)

        result = viewModel.checkNumberOfRowAndColumn(Int.MAX_VALUE + 1, limit)
        assert(result == EditError.NUMBER_TOO_SMALL)

        result = viewModel.checkNumberOfRowAndColumn(Int.MIN_VALUE - 1, limit)
        assert(result == EditError.NUMBER_TOO_LARGE)
    }
}