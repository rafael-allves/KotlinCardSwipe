package com.kotlinswipecard

import android.content.Context
import android.database.DataSetObserver
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.Adapter as AndroidAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kotlinswipecard.Swiper.SwipeHelper
import kotlin.random.Random

class CardStackView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : RecyclerView(context, attrs, defStyle) {

    private var animationStyle: String? = null
    private var stackOrientation: Int = 0

    private var adapter: AndroidAdapter? = null
    private var random: Random? = null

    private var animationDuration = 0
    private var currentViewIndex = 0
    private var numberOfStackedViews = 0
    private var viewSpacing = 0
    private var viewRotation = 0f
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

        // TODO: Implement custom attributes
    }

    private fun readAttributes(attributeSets: AttributeSet?){
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