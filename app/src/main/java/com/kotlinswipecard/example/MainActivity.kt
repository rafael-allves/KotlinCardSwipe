package com.kotlinswipecard.example

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.kotlinswipecard.CardStackManager
import com.kotlinswipecard.CardStackView
import com.kotlinswipecard.R
import com.kotlinswipecard.StackAdapter

class MainActivity : Activity() {
    private var cardStackView: CardStackView? = null
    private val cards: StackAdapter<CardModel> = StackAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        populateStack()
        setupCardStackView()
    }

    private fun setupCardStackView() {
        cardStackView = findViewById(R.id.card_stack_view)
        cardStackView?.layoutManager = CardStackManager(this)

        Log.d("MainActivity", "setupCardStackView: $cards")
        // Set adapter
        cardStackView?.adapter = CardStackAdapter(cards, object :
            CardAdapterInterface<CardModel> {
            override fun layoutId(): Int = R.layout.card_layout_example

            override fun bind(view: View, data: CardModel) {
                Log.d("CardStackView", "bind: $data")
                view.findViewById<TextView>(R.id.textView).text = data.name
                view.findViewById<TextView>(R.id.ageView).text = data.age.toString()
                view.setOnClickListener { Log.d("MainActivity", cards.pop(cardStackView?.adapter).name) }
                view.findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.teste)
            }
        })

        Log.d("MainActivity", cardStackView?.adapter?.itemCount.toString())
    }

    private fun populateStack() {
        // Prepare model data
        val cardModel = CardModel("John Doe", 25, "https://images.pexels.com/photos/20588094/pexels-photo-20588094.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2")
        cards.push(cardModel)
    }
}

