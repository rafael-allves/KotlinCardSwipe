package com.kotlinswipecard.lib.view

import android.content.Context
import android.database.DataSetObserver
import android.util.AttributeSet
import android.view.View
import com.kotlinswipecard.R
import com.kotlinswipecard.lib.swipe.SwipeHelper
import java.util.Random

class CardStackView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : AbstractCardStackView(context, attrs, defStyle) {

    private var animationStyle: String? = null
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
        //if (layoutManager == null) layoutManager = CardStackManager(context)
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

    private fun addNextView() {
        if(currentViewIndex < adapter!!.count){
            val viewHolder = findViewHolderForAdapterPosition(currentViewIndex) as? StackAdapter<*>.ViewHolder
            val bottomView = viewHolder?.contentView

            bottomView?.let { view ->
                view.setTag(R.id.new_view, true)

                if(disableHwAcceleration)
                    view.setLayerType(LAYER_TYPE_HARDWARE, null)

                if(viewRotation > 0)
                    view.rotation =
                        (random!!.nextInt(viewRotation) - viewRotation / 2).toFloat()
            }
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (adapter == null || adapter!!.isEmpty) {
            currentViewIndex = 0
            removeAllViewsInLayout()
        }
        var x = childCount
        while(x < numberOfStackedViews && currentViewIndex < adapter!!.count){
            addNextView()
            ++x
        }
        reorderItems()

        isFirstLayout = false
    }
}