package com.kotlinswipecard.lib.listeners

interface SwipeProgressListener {
    fun onSwipeStart(position: Int)

    fun onSwipeProgress(position: Int, progress: Float)

    fun onSwipeEnd(position: Int)
}