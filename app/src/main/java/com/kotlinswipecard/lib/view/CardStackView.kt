package com.kotlinswipecard.lib.view

import android.content.Context
import android.database.DataSetObserver
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.kotlinswipecard.R
import com.kotlinswipecard.lib.swipe.SwipeHelper
import java.util.Random

class CardStackView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : AbstractCardStackView(context, attrs, defStyle) {

    private var stackOrientation: Int = 0

    private var random: Random? = null

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

    private fun addNextView() {
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

    private fun reorderItems() {
        for(x in 0 until childCount){
            val childView = getChildAt(x)
            val topViewIndex = childCount - 1
            val distanceToViewAbove = topViewIndex * viewSpacing - x * viewSpacing
            val newPositionX = (width - childView.measuredWidth) / 2
            val newPositionY = distanceToViewAbove + paddingTop

            childView.layout(
                newPositionX,
                paddingTop,
                newPositionX + childView.measuredWidth,
                paddingTop + childView.measuredHeight
            )

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                childView.translationZ = x.toFloat()

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
}