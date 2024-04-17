package com.kotlinswipecard

import android.content.Context
import android.database.DataSetObserver
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.kotlinswipecard.Swiper.SwipeHelper
import java.util.Random

class CardStackView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : RecyclerView(context, attrs, defStyle) {

    private var animationStyle: String? = null
    private var stackOrientation: Int = 0

    private var adapter: StackAdapter<*>? = null
    private var random: Random? = null

    private var animationDuration = 0
    private var currentViewIndex = 0
    private var numberOfStackedViews = 0
    private var viewSpacing = 0
    private var viewRotation = 0
    private var swipeRotation = 0f
    private var swipeOpacity = 0f
    private var scaleFactor = 0f
    private var disableHwAcceleration = false
    private var isFirstLayout = true

    var topView: View? = null
        private set

    private var swipeHelper: SwipeHelper? = null
    private var dataObserver: DataSetObserver? = null
    private var listener: SwipeStackListener? = null
    private var progressListener: SwipeProgressListener? = null

    var allowedSwipeDirections = 0

    init {
        readAttributes(attrs)
        initialize()
    }

    private fun initialize() {

        if (layoutManager == null) layoutManager = CardStackManager(context)

        random = Random()
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

    override fun onSaveInstanceState(): Parcelable {
        val bundle = Bundle()
        bundle.putParcelable(KEY_SUPER_STATE, super.onSaveInstanceState())
        bundle.putInt(KEY_CURRENT_INDEX, currentViewIndex - childCount)
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        var mState: Parcelable? = state

        if(state is Bundle){
            val bundle = state
            currentViewIndex = bundle.getInt(KEY_CURRENT_INDEX)
            mState = bundle.getParcelable(KEY_SUPER_STATE)
        }

        super.onRestoreInstanceState(mState)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if(adapter == null || adapter!!.isEmpty){
            currentViewIndex = 0
            removeAllViewsInLayout()
        }
        super.onLayout(changed, l, t, r, b)
    }

    private fun readAttributes(attributeSets: AttributeSet?) {
        val attrs = context.obtainStyledAttributes(attributeSets, R.styleable.CardStackView)
        try {
            allowedSwipeDirections = attrs.getInt(
                R.styleable.CardStackView_alowed_swipe_directions,
                SWUPE_DIRECTION_BOTH
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


    override fun setAdapter(adapter: Adapter<*>?) {
        if (layoutManager == null) layoutManager = CardStackManager(context)

        super.setAdapter(adapter)
    }

    override fun onInterceptTouchEvent(e: MotionEvent?): Boolean {
        return super.onInterceptTouchEvent(e)
    }

}