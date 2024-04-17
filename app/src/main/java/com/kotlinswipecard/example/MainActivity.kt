package com.kotlinswipecard.example

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.kotlinswipecard.CardStackManager
import com.kotlinswipecard.lib.view.CardStackView
import com.kotlinswipecard.R
import com.kotlinswipecard.lib.view.StackAdapter

class MainActivity : Activity() {
    private var cardStackView: CardStackView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        populateStack()
        setupCardStackView()
    }
    private fun populateStack() {

        val cardModel = CardModel("John Doe", 25, "https://images.pexels.com/photos/20588094/pexels-photo-20588094.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2")
        cards.push(cardModel)
    }
}

