package com.kotlinswipecard.lib.animations

import android.view.View
import android.view.ViewPropertyAnimator
import android.view.animation.OvershootInterpolator

/**
 * A class that provides animation functionalities for swiping actions on views.
 * This includes resetting the view's position as well as animating horizontal swipes.
 */
class SwipesAnimator {

    /**
     * Animates the view back to its initial position with default properties reset.
     * This includes setting the position (x, y), rotation, and opacity back to their initial values.
     *
     * @param view The view to animate.
     * @param initialX The initial x-coordinate to reset the view's horizontal position.
     * @param initialY The initial y-coordinate to reset the view's vertical position.
     * @param animationDuration The duration of the animation in milliseconds.
     */
    fun animateResetPosition(view: View, initialX: Float, initialY: Float, animationDuration: Int) {
        view.animate()
            .x(initialX)
            .y(initialY)
            .rotation(0f)
            .alpha(1f)
            .setDuration(animationDuration.toLong())
            .setInterpolator(OvershootInterpolator(1.4f))
            .setListener(null)
    }

    /**
     * Initiates an animation for a horizontal swipe on the view, potentially exiting the screen.
     * This function cancels any ongoing animations before setting up the new one.
     *
     * @param view The view to animate.
     * @param positionX The target x-coordinate for the view after the swipe.
     * @param rotateDegress The degrees to rotate the view during the swipe.
     * @param duration The duration of the swipe animation in milliseconds.
     * @return The [ViewPropertyAnimator] instance controlling the animation, allowing further customization.
     */
    fun animateHorizontalSwipe(
        view: View,
        positionX: Float,
        rotateDegress: Float,
        duration: Int
    ): ViewPropertyAnimator {
        view.animate().cancel()
        return view.animate()
            .x(positionX)
            .rotation(rotateDegress)
            .alpha(0f)
            .setDuration(duration.toLong())
    }
}