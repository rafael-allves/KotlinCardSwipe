package com.kotlinswipecard.lib.view

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import java.util.Stack

abstract class StackAdapter<T>(private val stack: Stack<T>) :
    RecyclerView.Adapter<StackAdapter<T>.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val contentView: View = view;
    }

    private var _currentElement: T? = null

    val currentElement: T?
        get() = _currentElement

    val isEmpty: Boolean
        get() = stack.isEmpty()

    val count: Int
        get() = stack.size

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