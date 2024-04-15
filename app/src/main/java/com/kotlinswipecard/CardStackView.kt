package com.kotlinswipecard

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView

class CardStackView(context: Context) : RecyclerView(context) {

    constructor(context: Context, attrs: AttributeSet?) : this(context)

    override fun setAdapter(adapter: Adapter<*>?) {
        if (layoutManager == null) layoutManager = CardStackManager()

        super.setAdapter(adapter)
    }

    override fun onInterceptTouchEvent(e: MotionEvent?): Boolean {
        return super.onInterceptTouchEvent(e)
    }

}