package com.kotlinswipecard.lib.config

import androidx.annotation.IntDef
import androidx.recyclerview.widget.ItemTouchHelper

/**
 * Defines the allowable values for swipe directions in a swipeable component,
 * such as a list or a grid in a user interface. This annotation is used to ensure
 * that only valid swipe directions are used at compile time.
 *
 * The swipe directions are specified as flags, allowing for bitwise operations to
 * specify multiple directions. This can be particularly useful for setting behaviors
 * that are applicable to more than one direction.
 *
 * @property LEFT Indicates a swipe to the left. Maps to [ItemTouchHelper.LEFT].
 * @property UP Indicates a swipe upward. Maps to [ItemTouchHelper.UP].
 * @property RIGHT Indicates a swipe to the right. Maps to [ItemTouchHelper.RIGHT].
 * @property DOWN Indicates a swipe downward. Maps to [ItemTouchHelper.DOWN].
 * @property ALL Represents all swipe directions. It is a composite flag that combines
 * [LEFT], [UP], [RIGHT], and [DOWN] using a bitwise OR.
 *
 * @constructor Create empty Swipe direction
 */
@Retention(AnnotationRetention.SOURCE)
@IntDef(SwipeDirection.LEFT, SwipeDirection.UP, SwipeDirection.RIGHT, SwipeDirection.DOWN, flag = true)
annotation class SwipeDirection {
    companion object {
        const val LEFT = ItemTouchHelper.LEFT
        const val UP = ItemTouchHelper.UP
        const val RIGHT = ItemTouchHelper.RIGHT
        const val DOWN = ItemTouchHelper.DOWN

        /** Represents a combination of all defined swipe directions. */
        const val ALL = LEFT or UP or RIGHT or DOWN
    }
}
