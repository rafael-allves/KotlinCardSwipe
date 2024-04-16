package com.kotlinswipecard

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import java.util.Stack

class StackManager<T>: Stack<T>() {

    fun pop(adapter: Adapter<RecyclerView.ViewHolder>?): T {
        adapter?.notifyItemRemoved(size - 1)

        return super.pop()
    }

    fun push(item: T, adapter: Adapter<RecyclerView.ViewHolder>?): T {
        adapter?.notifyItemInserted(size - 1)

        return super.push(item)
    }

}