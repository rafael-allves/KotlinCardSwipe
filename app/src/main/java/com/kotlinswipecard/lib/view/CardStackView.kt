package com.kotlinswipecard.lib.view

import android.content.Context
import android.database.DataSetObserver
import android.util.AttributeSet
import android.view.View
import com.kotlinswipecard.R
import com.kotlinswipecard.lib.animations.SwipesAnimator
import com.kotlinswipecard.lib.swipe.SwipeHelper

class CardStackView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : AbstractCardStackView(context, attrs, defStyle) {

    private var stackOrientation: Int = 0

    var topView: View? = null
        private set

    private var swipeHelper: SwipeHelper? = null
    private var dataObserver: DataSetObserver? = null

    init {
        readAttributes(attrs)
        initialize()
    }

    private fun initialize() {
        if (layoutManager == null) layoutManager = CardStackManager(context)

        clipToPadding = false
        clipChildren = false
        swipeHelper = SwipeHelper(this)

        swipeHelper?.let { helper ->
            helper.setAnimationDuration(animationDuration)
            helper.setRotation(swipeRotation)
            helper.setOpacityEnd(swipeOpacity)
            dataObserver = object : DataSetObserver() {
                override fun onChanged() {
                    super.onChanged()
                    invalidate()
                    requestLayout()
                }
            }
        }
    }

    private fun reorderItems() {
        val topViewIndex = childCount - 1
        val midWidth = width / 2

        for (x in 0 until childCount) {
            val childView = getChildAt(x)
            val distanceToViewAbove = (topViewIndex - x) * viewSpacing
            val newPositionX = midWidth - childView.measuredWidth / 2
            val newPositionY = distanceToViewAbove + paddingTop

            childView.apply {
                layout(
                    newPositionX,
                    paddingTop,
                    newPositionX + measuredWidth,
                    paddingTop + measuredHeight
                )
                translationZ = x.toFloat()
                y = newPositionY.toFloat()
                scaleY = calculateScaleFactor(childCount - x)
                scaleX = scaleY
                alpha = if (childView.isNewView()) 0f else alpha
                setTag(R.id.new_view, false)
            }

            if (x == topViewIndex) {
                swipeHelper?.let { helper ->
                    helper.unRegisterObservedView()
                    topView = childView
                    helper.registerObservedView(
                        topView,
                        newPositionX.toFloat(),
                        newPositionY.toFloat()
                    )
                }
            }

            if (!isFirstLayout) {
                if (childView.isNewView())
                    childView.alpha = 0f
                SwipesAnimator.animateViewScale(
                    childView,
                    childView.scaleY,
                    newPositionY.toFloat(),
                    animationDuration
                )
            }
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (adapter == null || adapter!!.isEmpty) {
            currentViewIndex = 0
            removeAllViewsInLayout()
        }
        var x = childCount
        while (x < numberOfStackedViews && currentViewIndex < adapter!!.count) {
            addNextView()
            ++x
        }
        reorderItems()

        isFirstLayout = false
    }

    private fun removeTopView() {
        if (topView != null)
            adapter!!.pop()

        if (childCount == DEFAULT_STACK_SIZE / 2)
            listener!!.onStackMid()

        if (childCount == 0 && listener != null)
            listener!!.onStackEmpty()
    }

    fun onSwipeStart() {
        progressListener?.let { listener ->
            listener.onSwipeStart(currentPosition)
        }
    }

    fun onSwipeProgress(progress: Float) {
        progressListener?.let { listener ->
            listener.onSwipeProgress(
                currentPosition,
                progress
            )
        }
    }

    fun onViewSwipedToLeft() {
        progressListener?.let { listener ->
            listener.onViewSwipedToLeft(currentPosition)
        }
        removeTopView()
    }

    fun onViewSwipedToRight() {
        progressListener?.let {
            listener.onViewsSwipedToRight(currentPosition)
        }
        removeTopView()
    }


}