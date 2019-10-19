package com.teddy.tablegenerator.extensions

import android.app.Activity

fun Activity.getScreenSize(): Pair<Int, Int> {
    val display = this.windowManager.defaultDisplay
    val size = android.graphics.Point()
    display?.getSize(size)
    return Pair(size.x, size.y)
}