package com.kotlinswipecard

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import java.util.Stack

abstract class StackAdapter<T>(private val stack: Stack<T>) :
    RecyclerView.Adapter<StackAdapter.ViewHolder>() {
    abstract class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun getItemCount(): Int = stack.size

    val isEmpty: Boolean
        get() = stack.isEmpty()

    fun push(item: T) {
        stack.push(item)
        notifyItemInserted(stack.size - 1)
    }

    fun pop() {
        if (stack.isNotEmpty()) {
            notifyItemRemoved(stack.size - 1)
            stack.pop()
        }
    }

}