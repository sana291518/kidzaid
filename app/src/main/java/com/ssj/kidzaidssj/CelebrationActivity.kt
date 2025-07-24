package com.ssj.kidzaidssj

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import nl.dionsegijn.konfetti.xml.KonfettiView
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nl.dionsegijn.konfetti.core.models.Shape
import java.util.concurrent.TimeUnit

class CelebrationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_celebration)

        val konfettiView = findViewById<KonfettiView>(R.id.konfettiView)
        val jokeTextView = findViewById<TextView>(R.id.jokeTextView)

        // Get topic name from intent
        val topicName = intent.getStringExtra("TOPIC_NAME") ?: "General"

        // Set topic-specific joke
        jokeTextView.text = getCelebrationJoke(topicName)

        // Start confetti animation
        konfettiView.start(
            Party(
                speed = 10f,
                maxSpeed = 15f,
                damping = 0.9f,
                spread = 360,
                timeToLive = 3000L,
                position = Position.Relative(0.5, 0.3),
                shapes = listOf(Shape.Circle, Shape.Square),
                emitter = Emitter(duration = 2, TimeUnit.SECONDS).perSecond(100)
            )
        )
    }

    private fun getCelebrationJoke(topic: String): String {
        return when (topic) {
            "Fainting" -> "Fainted? You hit ‘Ctrl + Z’ on that real quick. 😎✨"
            "Choking" -> "That was close! Next time, just ‘Alt + F4’ that snack. 🍔😂"
            "Heart Attack" -> "Heart skipped a beat? Good thing you had a backup file! 💾❤️"
            "Seizure" -> "Seized the moment… and then let it go! ⏳😂"
            else -> "Mission accomplished! Another life hack unlocked. 🚀"
        }
    }
}
