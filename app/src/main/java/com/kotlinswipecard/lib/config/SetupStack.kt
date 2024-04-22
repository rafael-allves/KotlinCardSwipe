package com.kotlinswipecard.lib.config

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.kotlinswipecard.lib.listeners.SwipeStackListener

fun RecyclerView.setupStack(
    config: SwiperConfig,
    listener: SwipeStackListener,
    block: (RecyclerView.() -> Unit)? = null
) {
    layoutManager = StackLayoutManager(config)
    ItemTouchHelper(StackSwipeTouchHelperCallback(listener, config)).attachToRecyclerView(this)
    block?.invoke(this)
}