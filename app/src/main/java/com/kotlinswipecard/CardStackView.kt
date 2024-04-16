package com.kotlinswipecard

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView

class CardStackView : RecyclerView {

    private var animationStyle: String? = null
    private var stackOrientation: Int = 0
    constructor(context: Context): super(context)

    constructor(context: Context, attrs: AttributeSet?): super(context, attrs) {
        init(attrs, 0)
    }
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int = 0) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {

        if (layoutManager == null) layoutManager = CardStackManager(context)

        // TODO: Implement custom attributes
    }

    override fun setAdapter(adapter: Adapter<*>?) {
        if (layoutManager == null) layoutManager = CardStackManager(context)

        super.setAdapter(adapter)
    }

    override fun onInterceptTouchEvent(e: MotionEvent?): Boolean {
        return super.onInterceptTouchEvent(e)
    }

}