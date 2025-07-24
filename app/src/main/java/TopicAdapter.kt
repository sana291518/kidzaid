package com.ssj.kidzaidssj

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TopicAdapter(
    private val topics: List<Topic>,
    private val onItemClick: (Topic) -> Unit // Click listener function
) : RecyclerView.Adapter<TopicAdapter.TopicViewHolder>() {

    class TopicViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val topicImage: ImageView = view.findViewById(R.id.topicImage)
        val topicTitle: TextView = view.findViewById(R.id.topicTitle)
        val topicPickupLine: TextView = view.findViewById(R.id.topicPickupLine)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_topic, parent, false)
        return TopicViewHolder(view)
    }

    override fun onBindViewHolder(holder: TopicViewHolder, position: Int) {
        val topic = topics[position]
        if (topic.imageResId != 0) { // ✅ Ensure resource is valid
            holder.topicImage.setImageResource(topic.imageResId)
        }
        holder.topicTitle.text = topic.title
        holder.topicPickupLine.text = topic.pickupLine

        // ✅ Improved click listener
        holder.itemView.setOnClickListener {
            val pos = holder.adapterPosition
            if (pos != RecyclerView.NO_POSITION) {
                onItemClick(topics[pos])
            }
        }
    }

    override fun getItemCount(): Int = topics.size
}
