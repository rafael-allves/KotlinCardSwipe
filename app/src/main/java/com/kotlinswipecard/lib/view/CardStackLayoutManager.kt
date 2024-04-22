package com.kotlinswipecard.lib.view

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kotlinswipecard.lib.config.SwiperConfig

class CardStackLayoutManager(
    private val config: SwiperConfig = SwiperConfig()
) : RecyclerView.LayoutManager() {

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams =
        RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

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
}