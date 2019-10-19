package com.teddy.tablegenerator.ui.main

import androidx.lifecycle.ViewModel
import com.teddy.tablegenerator.consts.EditError

class MainViewModel : ViewModel() {
    fun checkNumberOfRowAndColumn(number: Int, limit: Int): EditError {
        return when {
            number > limit -> EditError.NUMBER_TOO_LARGE
            number <= 0 -> EditError.NUMBER_TOO_SMALL
            else -> EditError.NONE
        }
    }
}