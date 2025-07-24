package com.ssj.kidzaidssj

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.*

data class Question(
    val imageResId: Int,
    val options: List<String>,
    val correctAnswer: String
)

class QuizActivity : AppCompatActivity() {

    private lateinit var tts: TextToSpeech
    private lateinit var mediaPlayerYay: MediaPlayer
    private lateinit var mediaPlayerBoo: MediaPlayer

    private val questions = listOf(
        Question(R.drawable.sample_injury_image1, listOf("Bruise", "Cut", "Scrape"), "Bruise"),
        Question(R.drawable.sample_injury_image2, listOf("Bruise", "Cut", "Scrape"), "Cut"),
        Question(R.drawable.sample_injury_image3, listOf("Bruise", "Cut", "Scrape"), "Scrape")
    )

    private var currentQuestionIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        // Initialize UI elements
        val injuryImage: ImageView = findViewById(R.id.injuryImage)
        val option1: Button = findViewById(R.id.option1)
        val option2: Button = findViewById(R.id.option2)
        val option3: Button = findViewById(R.id.option3)

        // Load TTS for reading options aloud
        tts = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts.language = Locale.US
                loadQuestion()
            }
        }

        // Load sound effects
        mediaPlayerYay = MediaPlayer.create(this, R.raw.yay_sound)
        mediaPlayerBoo = MediaPlayer.create(this, R.raw.boo_sound)

        // Set click listeners for the buttons
        option1.setOnClickListener { checkAnswer(option1.text.toString()) }
        option2.setOnClickListener { checkAnswer(option2.text.toString()) }
        option3.setOnClickListener { checkAnswer(option3.text.toString()) }
    }

    private fun loadQuestion() {
        if (currentQuestionIndex < questions.size) {
            val question = questions[currentQuestionIndex]
            val injuryImage: ImageView = findViewById(R.id.injuryImage)
            injuryImage.setImageResource(question.imageResId)

            val options = question.options
            val option1: Button = findViewById(R.id.option1)
            val option2: Button = findViewById(R.id.option2)
            val option3: Button = findViewById(R.id.option3)

            option1.text = options[0]
            option2.text = options[1]
            option3.text = options[2]

            readOptionsAloud()
        } else {
            showCompletionPopup()
        }
    }

    private fun checkAnswer(selectedOption: String) {
        val correctAnswer = questions[currentQuestionIndex].correctAnswer

        if (selectedOption == correctAnswer) {
            mediaPlayerYay.start() // Play yay sound
            // Show success message with delay, but without the text
            showSuccessPopup() // Proceed directly to the next question
        } else {
            mediaPlayerBoo.start() // Play boo sound
            Toast.makeText(this, "Try Again!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun readOptionsAloud() {
        val optionsText = "Is this a ${questions[currentQuestionIndex].options.joinToString(", ")}?"
        tts.speak(optionsText, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    private fun showSuccessPopup() {
        // Instead of a popup message, we just move to the next question after a delay
        Handler().postDelayed({
            currentQuestionIndex++
            loadQuestion() // Load next question after the delay
        }, 2000) // 2000 milliseconds = 2 seconds
    }

    private fun showCompletionPopup() {
        val popupMessage = "Good job kid! I am proud of you!"
        tts.speak(popupMessage, TextToSpeech.QUEUE_FLUSH, null, null)

        // Show a Toast for completion
        Toast.makeText(this, popupMessage, Toast.LENGTH_LONG).show()

        // Optional: Navigate to the next activity after a delay
        // val intent = Intent(this, NextActivity::class.java)
        // startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        tts.shutdown()
        mediaPlayerYay.release()
        mediaPlayerBoo.release()
    }
}
