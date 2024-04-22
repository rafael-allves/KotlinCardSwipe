package com.kotlinswipecard.lib.utils

import android.view.View

interface CardAdapterInterface<T> {
    fun layoutId(): Int
    fun bind(view: View, data: T)
}