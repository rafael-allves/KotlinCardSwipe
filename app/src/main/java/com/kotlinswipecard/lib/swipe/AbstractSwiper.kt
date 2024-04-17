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
    protected var rotateDegreess: Float = 0f
    protected var opacityEnd: Float = 0f
    protected var animationDuration: Int = 0
    protected var initialX: Float = 0f
    protected var initialY: Float = 0f

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

    /**
     * Sets the duration for the swipe animations.
     *
     * @param duration Duration in milliseconds.
     */
    fun setAnimationDuration(duration: Int) {
        animationDuration = duration
    }

    /**
     * Sets the rotation angle for the swipe animation.
     *
     * @param rotation Rotation angle in degrees.
     */
    fun setRotation(rotation: Float) {
        rotateDegreess = rotation
    }

    /**
     * Sets the ending opacity for the swipe animation.
     *
     * @param alpha Ending opacity value from 0.0 (transparent) to 1.0 (opaque).
     */
    fun setOpacityEnd(alpha: Float) {
        opacityEnd = alpha
    }
}