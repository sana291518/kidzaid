package com.ssj.kidzaidssj

import android.animation.ObjectAnimator
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class HygieneQuizActivity : AppCompatActivity() {

    // Questions list
    private val questions = listOf(
        HygieneQuestion(
            "What is the first healthy thing Milo did after waking up?",
            R.drawable.wash_hands,
            2,
            R.drawable.correct_wash_hands,
            listOf( "Brush teeth", "Wash hands", "Drink water")
        ),
        HygieneQuestion(
            "How did Milo brush his teeth?",
            R.drawable.brush_teeth,
            3,
            R.drawable.correct_brush_teeth,
            listOf("No brushing", "Big circles", "Small circles")
        ),
        HygieneQuestion(
            "What did Milo do before eating his apple for breakfast?",
            R.drawable.rinse_apple,
            1,
            R.drawable.correct_rinse_apple,
            listOf("Rinse the apple", "Peel the apple", "Eat the apple without washing")
        ),
        HygieneQuestion(
            "What made Milo feel refreshed?",
            R.drawable.drink_water,
            1,
            R.drawable.correct_drink_water,
            listOf("Drink water", "Eat breakfast", "Go for a walk")
        )
    )

    private var currentQuestionIndex = 0

    // Views
    private lateinit var questionText: TextView
    private lateinit var questionImage: ImageView
    private lateinit var option1: Button
    private lateinit var option2: Button
    private lateinit var option3: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hygiene_quiz)

        // Initialize views
        questionText = findViewById(R.id.questionText)
        questionImage = findViewById(R.id.questionImage)
        option1 = findViewById(R.id.option1)
        option2 = findViewById(R.id.option2)
        option3 = findViewById(R.id.option3)

        // Load first question
        loadQuestion()

        // Button click listeners
        option1.setOnClickListener { checkAnswer(1) }
        option2.setOnClickListener { checkAnswer(2) }
        option3.setOnClickListener { checkAnswer(3) }
    }

    private fun loadQuestion() {
        val question = questions[currentQuestionIndex]
        questionText.text = question.questionText
        questionImage.setImageResource(question.questionImageRes)
        option1.text = question.options[0]
        option2.text = question.options[1]
        option3.text = question.options[2]
    }

    private fun checkAnswer(selectedOption: Int) {
        val question = questions[currentQuestionIndex]
        if (selectedOption == question.correctOption) {
            playCorrectAnswerEffects(question.correctAnswerImageRes)
        } else {
            vibratePhone()
            Toast.makeText(this, "Oops! Try again.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun playCorrectAnswerEffects(correctImageRes: Int) {
        val flipAnimation = ObjectAnimator.ofFloat(questionImage, "rotationY", 0f, 180f)
        flipAnimation.duration = 600
        flipAnimation.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                // Set the correct answer image
                questionImage.setImageResource(correctImageRes)

                // Flip back animation
                val flipBack = ObjectAnimator.ofFloat(questionImage, "rotationY", 180f, 360f)
                flipBack.duration = 600
                flipBack.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        // Play sound
                        val mediaPlayer = MediaPlayer.create(this@HygieneQuizActivity, R.raw.woho)
                        mediaPlayer.start()

                        // Move to the next question
                        Handler().postDelayed({
                            moveToNextQuestion()
                        }, 2000)
                    }
                })
                flipBack.start()
            }
        })
        flipAnimation.start()
    }

    private fun vibratePhone() {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val effect = VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE)
            vibrator.vibrate(effect)
        } else {
            vibrator.vibrate(300)
        }
    }

    private fun moveToNextQuestion() {
        if (currentQuestionIndex < questions.size - 1) {
            currentQuestionIndex++
            loadQuestion()
        } else {
            Toast.makeText(this, "Yayy! Quiz completed!", Toast.LENGTH_LONG).show()
            finish()
        }
    }
}
