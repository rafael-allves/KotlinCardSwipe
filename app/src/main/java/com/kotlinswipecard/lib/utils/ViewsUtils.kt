package com.kotlinswipecard.lib.utils

import android.view.View
import com.kotlinswipecard.lib.config.SwiperConfig
import kotlin.math.absoluteValue

/**
 * Scales the View uniformly on both X and Y axes.
 *
 * @param scale The scale factor to apply to both scaleX and scaleY properties of the View.
 */
fun View.scale(scale: Float) {
    scaleX = scale
    scaleY = scale
}

/**
 * Resets the View's scale to 1.0 (original size) and translations to zero, effectively restoring the View to its initial state.
 */
fun View.resetTransitions() {
    scale(1f) // Reset scale to 1.0
    translationX = 0f // Reset horizontal translation
    translationY = 0f // Reset vertical translation
}

/**
 * Scales the View based on its position within a stack and the current swipe ratio.
 * This method is typically used to dynamically scale views in a swipeable stack to indicate their position in the stack.
 *
 * @param config The configuration parameters for scaling, including the scale decrement per stack level.
 * @param pos The position of the View in the stack. 0 is the top of the stack.
 * @param ratio The current swipe ratio, used to adjust the scaling dynamically during a swipe gesture.
 */
fun View.scaleForPosition(config: SwiperConfig, pos: Int, ratio: Float) {
    // Calculate the scale factor based on the position and swipe ratio.
    // The further the position from the top (pos increases), the smaller the view should be.
    // The swipe ratio modifies the scale based on how much the user has swiped the view.
    scale(1 - pos * config.itemScale + ratio.absoluteValue * config.itemScale)
}

fun View.performSwipe(distanceX: Float, distanceY: Float){

}