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

fun View.translateForPosition(config: SwiperConfig, pos: Int, ratio: Float){
    val translation = config.itemTranslate
    when (config.stackDirection) {
        StackDirection.Left -> translationX = -(pos - ratio.absoluteValue) * measuredWidth * translation
        StackDirection.Up -> translationY = -(pos - ratio.absoluteValue) * measuredHeight * translation
        StackDirection.Right -> translationX = (pos - ratio.absoluteValue) * measuredWidth * translation
        StackDirection.Down -> translationY = (pos - ratio.absoluteValue) * measuredHeight * translation
    }
}

fun View.scaleForPosition(config: SwiperConfig, pos: Int, ratio: Float) {
    scale(1 - pos * config.itemScale + ratio.absoluteValue * config.itemScale)
}