package com.kotlinswipecard.lib.swipe

import android.view.View


/**
 * Abstract base class that handles swipe animations and interactions for a given [View].
 * It implements the [View.OnTouchListener] to intercept touch events and manipulate view properties
 * such as rotation, opacity, and animation duration based on user interactions.
 *
 * This class should be subclassed to define specific swipe behaviors.
 */
abstract class AbstractSwiper : View.OnTouchListener {
    protected var observedView: View? = null
    protected var listenForTouchEvents: Boolean = false
    protected var initialX: Float = 0f
    protected var initialY: Float = 0f

    var opacityEnd: Float = 0f
    var rotateDegreess: Float = 0f
    var animationDuration: Int = 0

    /**
     * Registers a [View] to be observed for touch events.
     * Initializes the position and touch listener of the view.
     *
     * @param view The view to observe for touch events.
     * @param initialX The initial horizontal position of the view.
     * @param initialY The initial vertical position of the view.
     */
    fun registerObservedView(view: View?, initialX: Float, initialY: Float) {
        if (view == null) return
        observedView = view
        observedView!!.setOnTouchListener(this)
        this.initialX = initialX
        this.initialY = initialY
        listenForTouchEvents = true
    }

    /**
     * Unregisters the currently observed view, removing its touch listener and clearing references.
     * This helps to prevent memory leaks and ensure proper cleanup.
     */
    fun unRegisterObservedView() {
        if (observedView != null)
            observedView!!.setOnTouchListener(null)

        observedView = null
        listenForTouchEvents = false
    }
}