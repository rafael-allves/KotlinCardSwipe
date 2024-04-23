package com.kotlinswipecard.example

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kotlinswipecard.lib.utils.CardAdapterInterface
import java.util.Stack

class ArrayAdapter(
    private val cards: Stack<CardModel>,
    private val configuration: CardAdapterInterface<CardModel>
): RecyclerView.Adapter<ArrayAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArrayAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(configuration.layoutId(), parent, false)
        return MyViewHolder(view)
    }

   fun removeFirstIndex() {
        cards.removeAt(0)
        notifyItemRemoved(0)
    }

    override fun onBindViewHolder(holder: ArrayAdapter.MyViewHolder, position: Int) {
        configuration.bind(holder.itemView, cards[position])
    }

    override fun getItemCount(): Int {
        return cards.size
    }
}