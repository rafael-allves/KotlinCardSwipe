package com.kotlinswipecard.example

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kotlinswipecard.lib.view.StackAdapter

class CardsAdapter<T>(
    private val items: StackAdapter<CardModel>,
    private val configurator: CardAdapterInterface<T>
) : CardStackAdapter<T>() {

    inner class CardViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        Log.d("MainActivity", "Adapter created")
        val view = LayoutInflater.from(parent.context)
            .inflate(configurator.layoutId(), parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        Log.d("MainActivity", "Adapter bound")
        configurator.bind(holder.itemView, items[position])
    }

    override fun getItemCount(): Int = items.size
}