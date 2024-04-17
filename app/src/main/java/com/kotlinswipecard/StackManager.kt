package com.kotlinswipecard

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import java.util.Stack

class StackManager<T>: Stack<T>() {

    private val lastPop: T? = null

    fun pop(adapter: Adapter<RecyclerView.ViewHolder>?): T {
        if ( size != 0 ) {
            adapter?.notifyItemRemoved(size - 1)

            return super.pop()
        }
        return lastPop!!
    }

    fun push(item: T, adapter: Adapter<RecyclerView.ViewHolder>?): T {
        adapter?.notifyItemInserted(size - 1)

        return super.push(item)
    }

}