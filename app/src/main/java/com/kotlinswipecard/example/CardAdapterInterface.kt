package com.kotlinswipecard.example

import android.view.View

interface CardAdapterInterface<T> {
    fun layoutId(): Int
    fun bind(view: View, data: CardModel)
}