package com.kotlinswipecard.lib.utils

import android.view.View
import com.kotlinswipecard.lib.config.StackDirection
import com.kotlinswipecard.lib.config.SwiperConfig
import kotlin.math.absoluteValue

fun View.scale(scale: Float) {
    scaleX = scale
    scaleY = scale
}

fun View.resetTransitions() {
    scale(1f)
    translationX = 0f
    translationY = 0f
}

fun View.scaleForPosition(config: SwiperConfig, pos: Int, ratio: Float) {
    scale(1 - pos * config.itemScale + ratio.absoluteValue * config.itemScale)
}