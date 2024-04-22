package com.kotlinswipecard.lib.view

import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.recyclerview.widget.RecyclerView
import com.kotlinswipecard.lib.config.StackDirection
import com.kotlinswipecard.lib.config.SwiperConfig
import com.kotlinswipecard.lib.utils.resetTransitions


/**
 * A custom [RecyclerView.LayoutManager] that arranges items in a stack-like fashion,
 * where items are scaled and translated based on their position in the stack.
 *
 * @property config Configuration parameters for controlling the behavior of the stack, such as scaling factor and direction of stack.
 */
class CardStackLayoutManager(
    private val config: SwiperConfig = SwiperConfig()
) : RecyclerView.LayoutManager() {

    /**
     * Generates default layout parameters for the RecyclerView items.
     * This layout manager requires items to fill their parent container.
     *
     * @return A new set of layout parameters with width and height set to [ViewGroup.LayoutParams.MATCH_PARENT].
     */
    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams =
        RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

    /**
     * Lays out children in the RecyclerView according to the stack configuration.
     * This method positions the visible children and applies scaling and translation effects based on their position in the stack.
     *
     * @param recycler The recycler to obtain view holders for position.
     * @param state The current state of RecyclerView, including the total number of items.
     */
    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        detachAndScrapAttachedViews(recycler)
        val showCount = config.showCount

        val itemCount = state.itemCount

        if (itemCount > showCount) {
            for (pos in showCount downTo 0) {
                val layout = recycler.getViewForPosition(pos)
                addAndMeasureView(layout)
                layout.translationZ = (-pos).toFloat()

                when {
                    pos == showCount -> {
                        layout.scaleAndTranslateForPosition(pos - 1)
                    }

                    pos > 0 -> {
                        layout.scaleAndTranslateForPosition(pos)
                    }

                    pos == 0 -> {
                        layout.resetTransitions()
                    }
                }
            }
        } else {
            for (pos in itemCount - 1 downTo 0) {
                val layout = recycler.getViewForPosition(pos)
                addAndMeasureView(layout)
                layout.translationZ = (-pos).toFloat()

                when {
                    pos > 0 -> {
                        layout.scaleAndTranslateForPosition(pos)
                    }

                    pos == 0 -> {
                        layout.resetTransitions()
                    }
                }
            }
        }
    }

    /**
     * Adds and measures a view within the RecyclerView's layout.
     *
     * This method is responsible for adding the view to the layout, measuring it,
     * and positioning it with margins centered horizontally and vertically.
     *
     * @param view The view to add and measure.
     */
    private fun addAndMeasureView(view: View) {
        addView(view)
        measureChildWithMargins(view, 0, 0)
        val decoratedMeasuredWidth = getDecoratedMeasuredWidth(view)
        val decoratedMeasuredHeight = getDecoratedMeasuredHeight(view)
        val widthSpace = (width - decoratedMeasuredWidth) / 2
        val heightSpace = (height - decoratedMeasuredHeight) / 2
        layoutDecoratedWithMargins(
            view,
            widthSpace, heightSpace,
            widthSpace + decoratedMeasuredWidth,
            heightSpace + decoratedMeasuredHeight
        )
    }

    /**
     * Scales and translates a view based on its position index.
     *
     * Applies scaling and translation animations depending on the configuration and the view's position in the stack.
     *
     * @receiver The view to apply scaling and translation.
     * @param positionIndex The index of the position which dictates the scale and translation of the view.
     */
    private fun View.scaleAndTranslateForPosition(positionIndex: Int) {
        val scale =
            1f - positionIndex * config.itemScale.coerceAtLeast(0f)
        val translationMultiplier = when (config.stackDirection) {
            StackDirection.Left -> -positionIndex
            StackDirection.Up -> -positionIndex
            StackDirection.Right -> positionIndex
            StackDirection.Down -> positionIndex
        }
        animate().apply {
            scaleX(scale)
            scaleY(scale)
            when (config.stackDirection) {
                StackDirection.Left, StackDirection.Right ->
                    translationX(translationMultiplier * measuredWidth * config.itemTranslate)

                StackDirection.Up, StackDirection.Down ->
                    translationY(translationMultiplier * measuredHeight * config.itemTranslate)
            }
            setDuration(300).setInterpolator(LinearInterpolator())
        }
    }
}