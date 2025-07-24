package com.ssj.kidzaidssj

import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class CardAdapter(
    private val items: List<CardItem>,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    class CardViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardTitle: TextView = view.findViewById(R.id.cardTitle)
        val cardContent: TextView = view.findViewById(R.id.cardContent)
        val optionsContainer: LinearLayout = view.findViewById(R.id.optionsContainer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_card_wrapper, parent, false)

        view.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )

        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val item = items[position]

        holder.cardTitle.text = item.title
        holder.cardTitle.textSize = 20f
        holder.cardContent.text = item.content
        holder.cardContent.textSize = 18f

        if (item.options != null && item.correctAnswerIndex != null) {
            holder.optionsContainer.visibility = View.VISIBLE
            holder.optionsContainer.removeAllViews()

            item.options.forEachIndexed { index, optionText ->
                val optionButton = Button(holder.itemView.context).apply {
                    text = optionText
                    textSize = 16f
                    setTextColor(Color.WHITE)
                    setPadding(16, 16, 16, 16)
                    setBackgroundColor(ContextCompat.getColor(context, R.color.light_blue))
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        setMargins(8, 8, 8, 8)
                    }

                    setOnClickListener {
                        // ✅ Disable all buttons after selection
                        for (i in 0 until holder.optionsContainer.childCount) {
                            holder.optionsContainer.getChildAt(i).isEnabled = false
                        }

                        // ✅ Immediately highlight user's selection
                        if (index == item.correctAnswerIndex) {
                            setBackgroundColor(Color.GREEN) // ✅ Correct answer
                        } else {
                            setBackgroundColor(Color.RED) // ❌ Wrong answer
                        }

                        // ✅ Show correct answer immediately
                        for (i in 0 until holder.optionsContainer.childCount) {
                            val correctButton = holder.optionsContainer.getChildAt(i) as Button
                            if (i == item.correctAnswerIndex) {
                                correctButton.setBackgroundColor(Color.GREEN) // ✅ Show correct answer
                            }
                        }

                        // ✅ Delay by 3 seconds before updating progress
                        Handler(Looper.getMainLooper()).postDelayed({
                            onItemClick(position) // ✅ Update progress
                        }, 2000) // ⏳ 3-second delay
                    }
                }
                holder.optionsContainer.addView(optionButton)
            }
        } else {
            holder.optionsContainer.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = items.size
}
