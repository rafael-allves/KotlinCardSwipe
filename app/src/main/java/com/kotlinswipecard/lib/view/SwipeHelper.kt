package com.kotlinswipecard.lib.view

import android.graphics.Canvas
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.kotlinswipecard.lib.config.StackDirection
import com.kotlinswipecard.lib.config.SwipeDirection
import com.kotlinswipecard.lib.config.SwiperConfig
import com.kotlinswipecard.lib.listeners.SwipeStackListener
import com.kotlinswipecard.lib.utils.scaleForPosition
import com.kotlinswipecard.lib.utils.xThreshold
import com.kotlinswipecard.lib.utils.yThreshold
import kotlin.math.absoluteValue

/**
 * A custom [ItemTouchHelper.Callback] that provides swipe functionality for a RecyclerView.
 * It integrates with a custom layout manager and handles swipe animations and callbacks based on the configured rules.
 *
 * @param listener A [SwipeStackListener] that handles swipe events.
 * @param config The configuration for the swipe behavior, including directions and translation.
 */
class SwipeHelper(
    private val listener: SwipeStackListener,
    private val config: SwiperConfig
) : ItemTouchHelper.Callback() {

    private var allowSwipe = true

    /**
     * Determines if swiping is enabled for the RecyclerView items.
     *
     * @return Boolean indicating if item view swipe is enabled.
     */
    override fun isItemViewSwipeEnabled(): Boolean = allowSwipe

    /**
     * Defines the allowed swipe directions for each ViewHolder within the RecyclerView.
     *
     * @param recyclerView The RecyclerView containing the items.
     * @param viewHolder The ViewHolder for which the movement flags are being queried.
     * @return Integer flags indicating the allowed swipe directions.
     */
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int = when {
        recyclerView.layoutManager !is CardStackLayoutManager -> 0
        recyclerView.isAnimating -> 0
        !listener.isSwipeAllowed(viewHolder) -> 0
        else -> config.swipeDirections
    }.let { swipeFlags ->
        makeMovementFlags(0, swipeFlags)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ) = false

    /**
     * Callback triggered when an item has been swiped by the user.
     *
     * @param viewHolder The ViewHolder that was swiped.
     * @param direction The direction in which the item was swiped.
     */
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        listener.onSwiped(viewHolder, direction)
    }

    /**
     * Adjusts the translation of a view based on its position and the swipe ratio.
     *
     * @param view The view to translate.
     * @param config Configuration settings for swipe behavior.
     * @param pos The position of the view in the stack.
     * @param ratio The swipe ratio determining how far along the swipe is.
     */
    private fun translateForPosition(view: View, config: SwiperConfig, pos: Int, ratio: Float) {
        val translation = config.itemTranslate
        view.apply {
            when (config.stackDirection) {
                StackDirection.Left -> translationX =
                    -(pos - ratio.absoluteValue) * measuredWidth * translation

                StackDirection.Up -> translationY =
                    -(pos - ratio.absoluteValue) * measuredHeight * translation

                StackDirection.Right -> translationX =
                    (pos - ratio.absoluteValue) * measuredWidth * translation

                StackDirection.Down -> translationY =
                    (pos - ratio.absoluteValue) * measuredHeight * translation
            }
        }
    }

    /**
     * Callback to draw the child views during a swipe gesture.
     *
     * @param c Canvas to draw on.
     * @param recyclerView The RecyclerView being drawn.
     * @param viewHolder The ViewHolder being interacted with.
     * @param dX Horizontal distance the view has been swiped.
     * @param dY Vertical distance the view has been swiped.
     * @param actionState The current action state.
     * @param isCurrentlyActive Flag indicating if there is an active swipe.
     */
    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        val itemView = viewHolder.itemView
        val ratioX = (dX / recyclerView.xThreshold).coerceIn(-1f, 1f)
        val ratio: Float
        val direction: Int
        if (dX.absoluteValue > dY.absoluteValue) {
            ratio = ratioX
            direction = if (dX > 0)
                SwipeDirection.RIGHT
            else
                SwipeDirection.LEFT
        } else if (dX.absoluteValue < dY.absoluteValue){
            ratio = (dY / recyclerView.yThreshold).coerceIn(-1f, 1f)
            direction = if (dY > 0)
                SwipeDirection.DOWN
            else
                SwipeDirection.UP
        } else {
            ratio = 0f
            direction = SwipeDirection.NONE

        }
        itemView.rotation = ratioX * config.itemRotation

        val childCount = recyclerView.childCount
        val start = if (childCount > config.showCount) 1 else 0

        for (pos in start until childCount - 1) {
            val idx = childCount - pos - 1
            val view = recyclerView.getChildAt(pos)
            view.scaleForPosition(config, idx, ratio)
            translateForPosition(view, config, idx, ratio)
        }
        listener.onSwipe(viewHolder, dX, dY, direction)
    }

    /**
     * Called when a ViewHolder has completed its swipe interaction, resetting its view state.
     *
     * @param recyclerView The RecyclerView containing the ViewHolder.
     * @param viewHolder The ViewHolder that was interacted with.
     */
    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        viewHolder.itemView.rotation = 0f
        listener.onSwipeEnd(viewHolder)
    }
}