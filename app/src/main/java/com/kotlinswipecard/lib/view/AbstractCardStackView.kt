package com.kotlinswipecard.lib.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.kotlinswipecard.R
import com.kotlinswipecard.lib.listeners.SwipeProgressListener
import com.kotlinswipecard.lib.listeners.SwipeStackListener
import java.util.Random
import kotlin.math.pow

abstract class AbstractCardStackView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : RecyclerView(context, attrs, defStyle) {

    protected var adapter: StackAdapter<*>? = null
    protected var currentViewIndex = 0

    protected var numberOfStackedViews = 0
    protected var viewSpacing = 0
    private var viewRotation = 0
    private var disableHwAcceleration = false
    protected var isFirstLayout = true
    protected var dataObserver: AdapterDataObserver? = null

    protected var animationDuration = 0

    private var scaleFactor = 0f

    protected var swipeRotation = 0f
    protected var swipeOpacity = 0f

    protected var swipeStackListener: SwipeStackListener? = null
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

    @SuppressLint("CustomViewStyleable")
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
        scaleFactor.toDouble().pow(position.toDouble()).toFloat()

    override fun setAdapter(adapter: Adapter<*>?) {
        if (!(adapter is StackAdapter<*>)) {
            val errorDetails =
                "Adapter type mismatch. Expected: StackAdapter<*>, Received: ${adapter?.javaClass?.canonicalName ?: "null"}"
            Log.e("CardStackView", "Failed to set adapter: $errorDetails")
            throw IllegalArgumentException(errorDetails)
        }

        if (this.adapter != null) this.adapter!!.unregisterAdapterDataObserver(dataObserver!!)
        this.adapter = adapter
        this.adapter!!.registerAdapterDataObserver(dataObserver!!)

    }

    fun setListener(listener: SwipeStackListener?) {
        this.swipeStackListener = listener
    }

    fun setSwipeProgressListener(listener: SwipeProgressListener?) {
        progressListener = listener
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
            currentViewIndex = state.getInt(KEY_CURRENT_INDEX)
            mState = state.getParcelable(KEY_SUPER_STATE)
        }

        super.onRestoreInstanceState(mState)
    }


    override fun onInterceptTouchEvent(e: MotionEvent?): Boolean {
        return super.onInterceptTouchEvent(e)
    }

    private fun configureViewDimensions(view: View) {
        val width = width - (paddingLeft + paddingRight)
        val height = height - (paddingTop + paddingBottom)
        val params = view.layoutParams ?: FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        val measureSpecWidth =
            if (params.width == FrameLayout.LayoutParams.MATCH_PARENT) MeasureSpec.EXACTLY
            else MeasureSpec.AT_MOST
        val measureSpecHeight =
            if (params.height == FrameLayout.LayoutParams.MATCH_PARENT) MeasureSpec.EXACTLY
            else MeasureSpec.AT_MOST
        view.measure(
            MeasureSpec.makeMeasureSpec(width, measureSpecWidth),
            MeasureSpec.makeMeasureSpec(height, measureSpecHeight)
        )
    }

    private fun configureViewProperties(view: View) {
        view.setTag(R.id.new_view, true)
        if (disableHwAcceleration)
            view.setLayerType(View.LAYER_TYPE_HARDWARE, null)

        if (viewRotation > 0)
            view.rotation = ((Random().nextInt(viewRotation) - viewRotation) / 2).toFloat()
    }

    private fun addConfiguredViewToLayout(view: View) {
        val params = view.layoutParams ?: FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        addViewInLayout(view, 0, params, true)
        currentViewIndex++
    }

    protected fun addNextView() {
        val adapterCount = adapter?.count ?: return
        if (currentViewIndex < adapterCount) {
            val viewHolder =
                findViewHolderForAdapterPosition(currentViewIndex) as? StackAdapter<*>.ViewHolder
            viewHolder?.contentView?.let { view ->
                configureViewDimensions(view)
                configureViewProperties(view)
                addConfiguredViewToLayout(view)
            }
        }
    }

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        setMeasuredDimension(
            MeasureSpec.getSize(widthSpec),
            MeasureSpec.getSize(heightSpec)
        )
    }
}