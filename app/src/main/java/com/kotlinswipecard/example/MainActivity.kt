package com.kotlinswipecard.example

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.kotlinswipecard.R
import com.kotlinswipecard.lib.config.StackDirection
import com.kotlinswipecard.lib.config.SwipeDirection
import com.kotlinswipecard.lib.config.SwiperConfig
import com.kotlinswipecard.lib.listeners.SwipeStackListener
import com.kotlinswipecard.lib.utils.CardAdapterInterface
import com.kotlinswipecard.lib.utils.setupStack
import java.util.Stack

class MainActivity : Activity() {
    private val cards: Stack<CardModel> = Stack()
    private var myAdapter: ArrayAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val config = SwiperConfig(
            showCount = 5,
            swipeDirections = SwipeDirection.ALL,
            stackDirection = StackDirection.Down,
            itemTranslate = 0.02f,
            itemRotation = 5f,
            itemScale = .0f
        )

        val recycler = findViewById<RecyclerView>(R.id.card_stack_view)

        myAdapter = ArrayAdapter(cards, object : CardAdapterInterface<CardModel> {
            override fun layoutId() = R.layout.card_layout_example

            override fun bind(view: View, data: CardModel) {
                view.findViewById<TextView>(R.id.textView).text = data.name
                view.findViewById<TextView>(R.id.ageView).text = data.age.toString()
                /*Glide.with(view)
                    .load(data.imageUrl)
                    .into(view.findViewById<ImageView>(R.id.imageView))*/
            }
        })

        val listener = object : SwipeStackListener {
            override fun onSwipe(
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                direction: Int
            ) {
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                when (direction) {
                    SwipeDirection.DOWN -> {
                        Log.d("onSwiped", "onSwiped($direction)")
                    }

                    SwipeDirection.LEFT -> {
                        Log.d("onSwiped", "onSwiped($direction)")
                    }

                    SwipeDirection.RIGHT -> {
                        Log.d("onSwiped", "onSwiped($direction)")
                    }

                    SwipeDirection.UP -> {
                        Log.d("onSwiped", "onSwiped($direction)")
                    }
                }

                myAdapter!!.removeFirstIndex()
            }

            override fun onSwipeEnd(viewHolder: RecyclerView.ViewHolder) {
                Log.d("onSwipeEnd", "" + myAdapter!!.itemCount)
            }

            override fun isSwipeAllowed(viewHolder: RecyclerView.ViewHolder) =
                viewHolder is ArrayAdapter.MyViewHolder
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
            "https://cdn.pixabay.com/photo/2024/01/24/15/10/ai-generated-8529788_960_720.jpg"
        )
        val cardModel2 = CardModel(
            "John Doe",
            25,
            "https://cdn.pixabay.com/photo/2024/01/24/15/10/ai-generated-8529788_960_720.jpg"
        )
        val cardModel3 = CardModel(
            "John Doe",
            25,
            "https://cdn.pixabay.com/photo/2024/01/24/15/10/ai-generated-8529788_960_720.jpg"
        )
        val cardModel4 = CardModel(
            "John Doe",
            25,
            "https://cdn.pixabay.com/photo/2024/01/24/15/10/ai-generated-8529788_960_720.jpg"
        )
        val cardModel5 = CardModel(
            "John Doe",
            25,
            "https://cdn.pixabay.com/photo/2024/01/24/15/10/ai-generated-8529788_960_720.jpg"
        )

        cards.push(cardModel5)
        cards.push(cardModel4)
        cards.push(cardModel3)
        cards.push(cardModel2)
        cards.push(cardModel)
    }
}

