package com.kotlinswipecard.example

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.kotlinswipecard.CardStackView
import com.kotlinswipecard.R

class MainActivity : Activity() {
    private var cardStackView: CardStackView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cardStackView = findViewById<CardStackView>(R.id.card_stack_view)

        // Prepare model data
        val cardModel = CardModel("John Doe", 25, "https://images.pexels.com/photos/20588094/pexels-photo-20588094.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=2")

        // Set adapter
        cardStackView?.adapter = CardStackAdapter(listOf(cardModel), object :
            CardAdapterInterface<CardModel> {
            override fun layoutId(): Int = R.layout.card_layout_example

            override fun bind(view: View, data: CardModel) {
                view.findViewById<TextView>(R.id.textView).text = data.name
                view.findViewById<TextView>(R.id.ageView).text = data.age.toString()
                view.findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.ic_launcher_background)
            }
        })
    }
}

data class CardModel(
    val name: String,
    val age: Int,
    val imageUrl: String
)