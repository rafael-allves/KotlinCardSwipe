package com.kotlinswipecard.example

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.kotlinswipecard.R
import com.kotlinswipecard.lib.config.StackDirection
import com.kotlinswipecard.lib.config.SwipeDirection
import com.kotlinswipecard.lib.config.SwiperConfig
import com.kotlinswipecard.lib.listeners.SwipeStackListener
import com.kotlinswipecard.lib.utils.setupStack
import java.util.Stack

class MainActivity : Activity() {
    private val cards: Stack<CardModel> = Stack()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val config = SwiperConfig(
            showCount = 5,
            swipeDirections = SwipeDirection.ALL,
            stackDirection = StackDirection.Down,
            itemTranslate = 0.02f,
            itemRotation = 15f,
            itemScale = .0f
        )

        val recycler = findViewById<RecyclerView>(R.id.card_stack_view)

        val listener = object : SwipeStackListener {
            override fun onSwipe(
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                direction: Int
            ) {
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                Log.d("onSwiped($direction)")

                itemAdapter.removeAt(viewHolder.bindingAdapterPosition)
            }

            override fun onSwipeEnd(viewHolder: RecyclerView.ViewHolder) {
                Log.d("onSwipeEnd()")
            }

            override fun isSwipeAllowed(viewHolder: RecyclerView.ViewHolder) = viewHolder is MyViewHolder
        }

        recycler.setupStack(
            config,
            listener
        ) {
            itemAnimator = DefaultItemAnimator().apply {
                addDuration = 200
                removeDuration = 200
            }
            adapter = myAdapter
        }

        populateStack()
    }

    private fun populateStack() {

        val cardModel = CardModel(
            "John Doe",
            25,
            "https://images.pexels.com/photos/20588094/pexels-photo-20588094.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2"
        )
        cards.push(cardModel)
    }
}

