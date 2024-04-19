package com.kotlinswipecard.lib.listeners

interface SwipeStackListener {
    fun onViewSwipedToLeft(position: Int)

    fun onViewSwipedToRight(position: Int)

    fun onStackMid()

    fun onStackEmpty()

}