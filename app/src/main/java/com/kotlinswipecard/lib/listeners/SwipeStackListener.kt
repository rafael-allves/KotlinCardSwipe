package com.kotlinswipecard.lib.listeners

import androidx.recyclerview.widget.RecyclerView
import com.kotlinswipecard.lib.config.SwipeDirection

/**
 * Interface definition for a callback to be invoked when a swipe event is triggered in a RecyclerView.
 * This interface enables handling swipe actions like drag movements, swipe completions, and checks to
 * determine if a swipe action is allowed on a particular ViewHolder.
 */
interface SwipeStackListener {

    /**
     * Called continuously as a swipe gesture is being performed.
     *
     * @param viewHolder The ViewHolder being swiped.
     * @param dX The horizontal displacement caused by the swipe. Positive values mean the swipe was to the right.
     * @param dY The vertical displacement caused by the swipe. Positive values mean the swipe was downward.
     * @param direction The direction of the swipe annotated with @SwipeDirection. This should represent predefined swipe directions.
     */
    fun onSwipe(
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        @SwipeDirection direction: Int
    )

    /**
     * Called when the swipe gesture on a ViewHolder is completed.
     *
     * @param viewHolder The ViewHolder that has been swiped.
     * @param direction The direction in which the swipe was completed. This parameter should be annotated with @SwipeDirection.
     */
    fun onSwiped(viewHolder: RecyclerView.ViewHolder, @SwipeDirection direction: Int)

    /**
     * Called when the swipe gesture ends, that is when the finger is lifted off the screen.
     *
     * @param viewHolder The ViewHolder on which the swipe action was performed.
     */
    fun onSwipeEnd(viewHolder: RecyclerView.ViewHolder)

    /**
     * Determines whether a swipe action is allowed on the specified ViewHolder.
     *
     * @param viewHolder The ViewHolder to be evaluated.
     * @return Boolean True if the swipe is allowed, false otherwise.
     */
    fun isSwipeAllowed(viewHolder: RecyclerView.ViewHolder): Boolean
}