package com.kotlinswipecard.lib.utils

import android.os.SystemClock
import android.view.InputDevice
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.kotlinswipecard.lib.config.SwiperConfig
import com.kotlinswipecard.lib.listeners.SwipeStackListener
import com.kotlinswipecard.lib.view.CardStackLayoutManager
import com.kotlinswipecard.lib.view.SwipeHelper

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

/**
 * Performs a swipe gesture on a `RecyclerView` from a target view's position.
 *
 * @receiver The `RecyclerView` on which the swipe gesture will be performed.
 * @param target The `View` from which the swipe gesture starts.
 * @param distanceX The distance to swipe in the X direction.
 * @param distanceY The distance to swipe in the Y direction.
 *
 * This function simulates a swipe gesture on a `RecyclerView`, starting from the
 * target view's position and moving by the specified distances in the X and Y directions.
 * The swipe gesture is divided into a number of steps to create a smooth movement.
 *
 * The function obtains the initial coordinates of the target view in both global and
 * local coordinate systems, then generates and dispatches a sequence of touch events
 * (`MotionEvent`) to simulate the swipe.
 *
 * Example usage:
 * ```
 * recyclerView.performSwipe(targetView, 500f, 0f) // Swipe 500 pixels to the right
 * recyclerView.performSwipe(targetView, 0f, 500f) // Swipe 500 pixels downwards
 * ```
 *
 * Note: Ensure that the `RecyclerView` and the target view are properly initialized and
 * visible before calling this function. This function is intended for testing purposes.
 */
fun RecyclerView.performSwipe(target: View, distanceX: Float, distanceY: Float) {
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

    this.dispatchTouchEvent(
        MotionEvent.obtain(
            downTime,
            eventTime,
            MotionEvent.ACTION_DOWN,
            initGlobalX,
            initGlobalY,
            0
        ).apply {
            setLocation(initLocalX, initLocalY)
            source = InputDevice.SOURCE_TOUCHSCREEN
        })

    val steps = 20
    var i = 0

    while (i in 0..steps) {
        val globalX = initGlobalX + i * distanceX / steps
        val globalY = initGlobalY + i * distanceY / steps
        val localX = initLocalX + i * distanceX / steps
        val localY = initLocalY + i * distanceY / steps

        if (globalX <= 10f || globalY <= 10f)
            break

        this.dispatchTouchEvent(
            MotionEvent.obtain(
                downTime,
                ++eventTime,
                MotionEvent.ACTION_MOVE,
                globalX,
                globalY,
                0
            ).apply {
                setLocation(localX, localY)
                source = InputDevice.SOURCE_TOUCHSCREEN
            }
        )
        ++i
    }

    this.dispatchTouchEvent(
        MotionEvent.obtain(
            downTime,
            ++eventTime,
            MotionEvent.ACTION_UP,
            initGlobalX + i * distanceX,
            initGlobalY + i * distanceY,
            0
        ).apply {
            setLocation(initLocalX + i * distanceX, initLocalY + i * distanceY)
            source = InputDevice.SOURCE_TOUCHSCREEN
        }
    )
}

fun RecyclerView.performSwipeToLeft(target: View) {
    this.performSwipe(target, distanceX = -this.width * .5f, distanceY = 0f)
}

fun RecyclerView.performSwipeToRight(target: View) {
    this.performSwipe(target, distanceX = +this.width * .5f, distanceY = 0f)
}

fun RecyclerView.performSwipeToTop(target: View) {
    this.performSwipe(target, distanceX = 0f, distanceY = -this.height * .5f)
}

fun RecyclerView.performSwipeToBottom(target: View) {
    this.performSwipe(target, distanceX = 0f, distanceY = +this.width * .5f)
}