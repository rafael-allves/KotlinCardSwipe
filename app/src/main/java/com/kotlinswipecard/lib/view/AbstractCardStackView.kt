package com.kotlinswipecard.lib.view

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.kotlinswipecard.R

abstract class AbstractCardStackView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : RecyclerView(context, attrs, defStyle) {

    protected var adapter: StackAdapter<*>? = null
    protected var currentViewIndex = 0

    protected var numberOfStackedViews = 0
    protected var viewSpacing = 0
    protected var viewRotation = 0
    protected var disableHwAcceleration = false
    protected var isFirstLayout = true

    protected var animationDuration = 0

    protected var scaleFactor = 0f

    protected var swipeRotation = 0f
    protected var swipeOpacity = 0f

    protected var listener: SwipeStackListener? = null
    protected var progressListener: SwipeProgressListener? = null

    var allowedSwipeDirections = 0

    val currentPosition: Int
        get() = currentViewIndex - childCount

    companion object {
        const val SWIPE_DIRECTION_BOTH = 0
        const val SWIPE_DIRECTION_ONLY_LEFT = 1
        const val SWIPE_DIRECTION_ONLY_RIGHT = 2
        const val DEFAULT_ANIMATION_DURATION = 300
        const val DEFAULT_STACK_SIZE = 6
        const val DEFAULT_STACK_ROTATION = 8
        const val DEFAULT_SWIPE_ROTATION = 30f
        const val DEFAULT_SWIPE_OPACITY = 1f
        const val DEFAULT_SCALE_FACTOR = 1f
        const val DEFAULT_DISABLE_HW_ACCELERATION = true

        @JvmStatic
        protected val KEY_SUPER_STATE = "superState"

        @JvmStatic
        protected val KEY_CURRENT_INDEX = "currentIndex"
    }

    protected fun readAttributes(attributeSets: AttributeSet?) {
        val attrs = context.obtainStyledAttributes(attributeSets, R.styleable.CardStackView)
        try {
            allowedSwipeDirections = attrs.getInt(
                R.styleable.CardStackView_alowed_swipe_directions,
                SWIPE_DIRECTION_BOTH
            )

            animationDuration = attrs.getInt(
                R.styleable.CardStackView_animation_duration,
                DEFAULT_ANIMATION_DURATION
            )

            viewSpacing = attrs.getDimensionPixelOffset(
                R.styleable.CardStackView_stack_spacing,
                resources.getDimensionPixelSize(R.dimen.card_stack_default_spacing)
            )

            viewRotation =
                attrs.getInt(R.styleable.CardStackView_stack_rotation, DEFAULT_STACK_ROTATION)

            swipeRotation =
                attrs.getFloat(R.styleable.CardStackView_swipe_rotation, DEFAULT_SWIPE_ROTATION)

            swipeOpacity =
                attrs.getFloat(R.styleable.CardStackView_swipe_opacity, DEFAULT_SWIPE_OPACITY)

            scaleFactor =
                attrs.getFloat(R.styleable.CardStackView_scale_factor, DEFAULT_SCALE_FACTOR)

            disableHwAcceleration = attrs.getBoolean(
                R.styleable.CardStackView_disable_hw_acceleration,
                DEFAULT_DISABLE_HW_ACCELERATION
            )

        } finally {
            attrs.recycle()
        }
    }

    protected fun View.isNewView(): Boolean = getTag(R.id.new_view) as? Boolean ?: false

    protected fun calculateScaleFactor(position: Int): Float =
        Math.pow(scaleFactor.toDouble(), position.toDouble()).toFloat()

    override fun setAdapter(adapter: Adapter<*>?) {
        if (!(adapter is StackAdapter<*>)) {
            val errorDetails =
                "Adapter type mismatch. Expected: StackAdapter<*>, Received: ${adapter?.javaClass?.canonicalName ?: "null"}"
            Log.e("CardStackView", "Failed to set adapter: $errorDetails")
            throw IllegalArgumentException(errorDetails)
        }
        super.setAdapter(adapter)
    }

    override fun onSaveInstanceState(): Parcelable {
        val bundle = Bundle()
        bundle.putParcelable(KEY_SUPER_STATE, super.onSaveInstanceState())
        bundle.putInt(KEY_CURRENT_INDEX, currentViewIndex - childCount)
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        var mState: Parcelable? = state

        if (state is Bundle) {
            val bundle = state
            currentViewIndex = bundle.getInt(KEY_CURRENT_INDEX)
            mState = bundle.getParcelable(KEY_SUPER_STATE)
        }

        super.onRestoreInstanceState(mState)
    }


    override fun onInterceptTouchEvent(e: MotionEvent?): Boolean {
        return super.onInterceptTouchEvent(e)
    }
}