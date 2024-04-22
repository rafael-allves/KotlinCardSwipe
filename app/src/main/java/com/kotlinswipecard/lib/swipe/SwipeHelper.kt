package com.kotlinswipecard.lib.swipe

import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.kotlinswipecard.lib.config.SwipeDirection
import com.kotlinswipecard.lib.config.SwiperConfig
import com.kotlinswipecard.lib.listeners.SwipeStackListener
import com.kotlinswipecard.lib.utils.scaleForPosition
import com.kotlinswipecard.lib.utils.xThreshold
import com.kotlinswipecard.lib.utils.yThreshold
import com.kotlinswipecard.lib.view.CardStackLayoutManager
import kotlin.math.absoluteValue

class SwipeHelper(
    private val listener: SwipeStackListener,
    private val config: SwiperConfig
) : ItemTouchHelper.Callback() {

    private var allowSwipe = true

    override fun isItemViewSwipeEnabled(): Boolean = allowSwipe

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

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        listener.onSwiped(viewHolder, direction)
    }

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
            direction = if (dX > 0) {
                SwipeDirection.RIGHT
            } else {
                SwipeDirection.LEFT
            }
        } else {
            ratio = (dY / recyclerView.yThreshold).coerceIn(-1f, 1f)
            direction = if (dY > 0) {
                SwipeDirection.DOWN
            } else {
                SwipeDirection.UP
            }
        }
        itemView.rotation = ratioX * config.itemRotation

        val childCount = recyclerView.childCount
        val start = if (childCount > config.showCount) 1 else 0

        for (pos in start until childCount - 1) {
            val idx = childCount - pos - 1
            val view = recyclerView.getChildAt(pos)
            view.scaleForPosition(config, idx, ratio)
            view.translateForPosition(config, idx, ratio)
        }
        listener.onSwipe(viewHolder, dX, dY, direction)
    }


    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        viewHolder.itemView.rotation = 0f
        listener.onSwipeEnd(viewHolder)
    }
}