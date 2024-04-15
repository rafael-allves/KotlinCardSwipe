package com.kotlinswipecard

import android.content.Context
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView

class CardStackView(context: Context) : RecyclerView(context) {

    override fun setAdapter(adapter: Adapter<*>?) {
        super.setAdapter(adapter)
    }

    override fun onInterceptTouchEvent(e: MotionEvent?): Boolean {
        return super.onInterceptTouchEvent(e)
    }

}