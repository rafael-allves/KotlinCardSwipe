package com.kotlinswipecard.lib.utils

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.kotlinswipecard.lib.config.SwiperConfig
import com.kotlinswipecard.lib.listeners.SwipeStackListener
import com.kotlinswipecard.lib.view.CardStackLayoutManager

fun RecyclerView.setupStack(
    config: SwiperConfig,
    listener: SwipeStackListener,
    block: (RecyclerView.() -> Unit)? = null
) {
    layoutManager = CardStackLayoutManager(config)
    ItemTouchHelper(
        StackSwipeTouchHelperCallback(
            listener,
            config
        )
    ).attachToRecyclerView(this)
    block?.invoke(this)
}