package com.kotlinswipecard.lib.swipe

import android.animation.Animator
import android.view.MotionEvent
import android.view.View
import com.kotlinswipecard.lib.animations.AnimationUtil
import com.kotlinswipecard.lib.animations.SwipesAnimator
import com.kotlinswipecard.lib.view.AbstractCardStackView
import com.kotlinswipecard.lib.view.CardStackView
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class SwipeHelper(private val swipeStack: CardStackView) : AbstractSwiper() {
    private var downX: Float = 0f
    private var downY: Float = 0f
    private var pointerId: Int = 0

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                if (!listenForTouchEvents || !swipeStack.isEnabled)
                    return false

                v?.parent?.requestDisallowInterceptTouchEvent(true)
                swipeStack.onSwipeStart()
                pointerId = event.getPointerId(0)
                downX = event.getX(pointerId)
                downY = event.getY(pointerId)

                return true
            }

            MotionEvent.ACTION_MOVE -> {
                observedView?.let { view ->
                    val pointerIndex = event.findPointerIndex(pointerId)
                    if (pointerIndex < 0) return false
                    val dx = event.getX(pointerIndex) - downX
                    val dy = event.getY(pointerIndex) - downY

                    view.x += dx
                    view.y += dy

                    val dragDistanceX = view.x - initialX
                    val swipeProgress = min(
                        max(
                            dragDistanceX / swipeStack.width, -1f
                        ), 1f
                    )
                    swipeStack.onSwipeProgress(swipeProgress)

                    if (rotateDegreess > 0) {
                        val rotation = rotateDegreess * swipeProgress
                        view.rotation = rotation
                    }

                    if (opacityEnd < 1f)
                        view.alpha = 1 - min(abs(swipeProgress * 2), 1f)

                    return true
                }
            }

            MotionEvent.ACTION_UP -> {
                v?.parent?.requestDisallowInterceptTouchEvent(false)
                swipeStack.onSwipeEnd()
                checkViewPosition()
                return true
            }
        }
        return false
    }

    private fun checkViewPosition() {
        if (!swipeStack.isEnabled) {
            resetViewPosition()
            return
        }
        val viewCenterHorizontal = observedView!!.x + observedView!!.width / 2
        val parentFirstThird = swipeStack.width / 3f
        val parentLastThird = parentFirstThird * 2

        if (viewCenterHorizontal < parentFirstThird &&
            swipeStack.allowedSwipeDirections != AbstractCardStackView.SWIPE_DIRECTION_ONLY_RIGHT
        )
            swipeViewToLeft(animationDuration / 2)
        else if (viewCenterHorizontal > parentLastThird &&
            swipeStack.allowedSwipeDirections != AbstractCardStackView.SWIPE_DIRECTION_ONLY_LEFT
        )
            swipeViewToRight(animationDuration / 2)
        else
            resetViewPosition()
    }

    private fun resetViewPosition() {
        SwipesAnimator.animateResetPosition(observedView!!, initialX, initialY, animationDuration)
    }

    private fun swipeViewToLeft(duration: Int) {
        if (!listenForTouchEvents) return
        listenForTouchEvents = false
        observedView?.let { view ->
            SwipesAnimator.animateHorizontalSwipe(
                view,
                -swipeStack.width + view.x,
                -rotateDegreess,
                duration
            )
                .setListener(object : AnimationUtil.AnimationEndListener() {
                    override fun onAnimationEnd(animation: Animator) {
                        swipeStack.onViewSwipedToLeft()
                    }
                })
        }
    }

    private fun swipeViewToRight(duration: Int) {
        if (!listenForTouchEvents) return
        listenForTouchEvents = false
        observedView?.let { view ->
            SwipesAnimator.animateHorizontalSwipe(
                view,
                swipeStack.width + view.x,
                rotateDegreess,
                duration
            )
                .setListener(object : AnimationUtil.AnimationEndListener() {
                    override fun onAnimationEnd(animation: Animator) {
                        swipeStack.onViewSwipedToRight()
                    }
                })
        }
    }

    fun swipeViewToLeft() {
        swipeViewToLeft(animationDuration)
    }

    fun swipeViewToRight() {
        swipeViewToRight(animationDuration)
    }

}