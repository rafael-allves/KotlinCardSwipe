package com.kotlinswipecard.example

import android.app.Activity
import android.os.Bundle
import com.kotlinswipecard.R
import com.kotlinswipecard.lib.view.CardStackView
import java.util.Stack

class MainActivity : Activity() {
    private var cardStackView: CardStackView? = null
    private val cards: Stack<CardModel> = Stack()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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

