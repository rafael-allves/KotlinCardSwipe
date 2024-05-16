package com.kotlinswipecard.lib.utils

import android.os.SystemClock
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.kotlinswipecard.lib.config.SwiperConfig
import com.kotlinswipecard.lib.listeners.SwipeStackListener
import com.kotlinswipecard.lib.view.SwipeHelper
import com.kotlinswipecard.lib.view.CardStackLayoutManager

/**
 * Configures the RecyclerView to use CardStackLayoutManager and attaches a SwipeHelper
 * for handling swipe gestures according to the specified configuration and listener.
 * This method sets up the RecyclerView for swipe functionalities typical in card stack UIs,
 * such as those found in dating apps or decision-making apps.
 *
 * @param config The SwiperConfig that defines behavior and visual properties of the swipe stack.
 * @param listener The SwipeStackListener that will handle swipe events.
 * @param block An optional block that allows further customization of the RecyclerView. This block
 *              will be executed after setting up the layout manager and the swipe helper.
 */
fun RecyclerView.setupStack(
    config: SwiperConfig,
    listener: SwipeStackListener,
    block: (RecyclerView.() -> Unit)? = null
) {
    layoutManager = CardStackLayoutManager(config)
    ItemTouchHelper(
        SwipeHelper(
            listener,
            config
        )
    ).attachToRecyclerView(this) // Attach the ItemTouchHelper with the SwipeHelper.
    block?.invoke(this) // Execute additional configuration block if provided.
}

/**
 * Extension property to determine the horizontal swipe threshold for the RecyclerView.
 * This threshold is used to decide when a swipe has been significant enough to trigger a swipe event.
 * It is calculated as 40% of the RecyclerView's width.
 */
val RecyclerView.xThreshold: Float
    get() = width * 0.4f

/**
 * Extension property to determine the vertical swipe threshold for the RecyclerView,
 * which is set equal to the horizontal swipe threshold.
 * This is used in determining the vertical swipe gestures' significance.
 */
val RecyclerView.yThreshold: Float
    get() = xThreshold


fun ViewGroup.performSwipe(target: View, distanceX: Float, distanceY: Float) {
    val parentCoords = intArrayOf(0, 0)
    this.getLocationInWindow(parentCoords)

    val childCoords = intArrayOf(0, 0)
    target.getLocationInWindow(childCoords)

    val initGlobalX = childCoords[0].toFloat() + 1f
    val initGlobalY = childCoords[1].toFloat() + 1f

    val initLocalX = (childCoords[0] - parentCoords[0]).toFloat() + 1f
    val initLocalY = (childCoords[1] - parentCoords[1]).toFloat() + 1f

    val downTime = SystemClock.uptimeMillis()
    var eventTime = SystemClock.uptimeMillis()
}