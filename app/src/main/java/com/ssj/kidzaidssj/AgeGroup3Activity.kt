package com.ssj.kidzaidssj

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AgeGroup3Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_age_group3)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        val topics = listOf(
            Topic("Fainting", "Stay calm, help is here!", R.drawable.fainting_image),
            Topic("Choking", "Act fast, save a life!", R.drawable.choking_image),
            Topic("Heart Attack", "Know the signs, act now!", R.drawable.heart_attack_image),
            Topic("Epileptic Seizure", "Stay safe, stay informed!", R.drawable.seizure_image)
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = TopicAdapter(topics) { selectedTopic ->
            val intent = Intent(this, TopicActivity::class.java)
            intent.putExtra("TOPIC_NAME", selectedTopic.title)
            startActivity(intent)
        }

    }
}

