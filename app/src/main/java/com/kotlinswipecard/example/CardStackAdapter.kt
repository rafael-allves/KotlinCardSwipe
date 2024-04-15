package com.kotlinswipecard.example

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class CardStackAdapter<T>(
    private val items: List<T>,
    private val configurator: CardAdapterInterface<T>
) : RecyclerView.Adapter<CardStackAdapter<T>.CardViewHolder>() {

    inner class CardViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(configurator.layoutId(), parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        configurator.bind(holder.itemView, items[position])
    }

    override fun getItemCount(): Int = items.size
}