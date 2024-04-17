package com.kotlinswipecard.Animator

import android.view.View
import android.view.ViewPropertyAnimator
import android.view.animation.OvershootInterpolator
import java.time.Duration

class SwipesAnimator {
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

    fun animateHorizontalSwipe(view: View, positionX: Float, rotateDegress: Float, duration: Int): ViewPropertyAnimator{
        view.animate().cancel()
        return view.animate()
            .x(positionX)
            .rotation(rotateDegress)
            .alpha(0f)
            .setDuration(duration.toLong())
    }
}