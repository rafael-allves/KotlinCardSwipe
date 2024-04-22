package com.kotlinswipecard.lib.utils

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.kotlinswipecard.lib.config.SwiperConfig
import com.kotlinswipecard.lib.listeners.SwipeStackListener
import com.kotlinswipecard.lib.swipe.SwipeHelper
import com.kotlinswipecard.lib.view.CardStackLayoutManager

fun RecyclerView.setupStack(
    config: SwiperConfig,
    listener: SwipeStackListener,
    block: (RecyclerView.() -> Unit)? = null
) {
    layoutManager = CardStackLayoutManager(config)
    ItemTouchHelper(
        SwipeHelper(
            listener,
            config
        )
    ).attachToRecyclerView(this)
    block?.invoke(this)
}

val RecyclerView.xThreshold get() = width * .4f
val RecyclerView.yThreshold get() = xThreshold