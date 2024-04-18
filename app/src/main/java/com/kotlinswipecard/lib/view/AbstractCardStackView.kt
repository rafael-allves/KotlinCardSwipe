package com.kotlinswipecard.lib.view

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView

abstract class AbstractCardStackView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : RecyclerView(context, attrs, defStyle) {
    companion object {
        const val SWIPE_DIRECTION_BOTH = 0
        const val SWIPE_DIRECTION_ONLY_LEFT = 1
        const val SWIPE_DIRECTION_ONLY_RIGHT = 2
        const val DEFAULT_ANIMATION_DURATION = 300
        const val DEFAULT_STACK_SIZE = 6
        const val DEFAULT_STACK_ROTATION = 8
        const val DEFAULT_SWIPE_ROTATION = 30f
        const val DEFAULT_SWIPE_OPACITY = 1f
        const val DEFAULT_SCALE_FACTOR = 1f
        const val DEFAULT_DISABLE_HW_ACCELERATION = true
        private const val KEY_SUPER_STATE = "superState"
        private const val KEY_CURRENT_INDEX = "currentIndex"
    }
}