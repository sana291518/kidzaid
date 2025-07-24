package com.ssj.kidzaidssj

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var remainingTimeText: TextView
    private val handler = Handler(Looper.getMainLooper())

    private val usageLimit = 30 * 60 * 1000 // 30 minutes in milliseconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tts = TextToSpeech(this, this)
        sharedPreferences = getSharedPreferences("AppUsagePrefs", MODE_PRIVATE)
        remainingTimeText = findViewById(R.id.remainingTimeText)

        val bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce)

        val buttonAgeGroup1: Button = findViewById(R.id.buttonAgeGroup1)
        val buttonAgeGroup2: Button = findViewById(R.id.buttonAgeGroup2)
        val buttonAgeGroup3: Button = findViewById(R.id.buttonAgeGroup3)
        val welcomeMessage: TextView = findViewById(R.id.welcomeMessage)

        buttonAgeGroup1.text = createColorfulText("4-6 Years")
        buttonAgeGroup2.text = createColorfulText("7-9 Years")
        buttonAgeGroup3.text = createColorfulText("10-12 Years")

        buttonAgeGroup1.startAnimation(bounceAnimation)
        buttonAgeGroup2.startAnimation(bounceAnimation)
        buttonAgeGroup3.startAnimation(bounceAnimation)

        buttonAgeGroup1.setOnClickListener { checkTimeLimitAndNavigate(AgeGroup1Activity::class.java) }
        buttonAgeGroup2.setOnClickListener { checkTimeLimitAndNavigate(AgeGroup2Activity::class.java) }
        buttonAgeGroup3.setOnClickListener { checkTimeLimitAndNavigate(AgeGroup3Activity::class.java) }
        welcomeMessage.setOnClickListener { playWelcomeMessage() }

        updateRemainingTimeUI()
        startTrackingUsage()
    }

    private fun createColorfulText(text: String): SpannableString {
        val spannable = SpannableString(text)
        val colors = intArrayOf(
            -0x10000, // Red
            -0xff0100, // Blue
            -0xff0100, // Green
            -0x100, // Yellow
            -0xffff01, // Magenta
            -0xff0001, // Cyan
            -0x333334 // Gray
        )
        for (i in text.indices) {
            spannable.setSpan(ForegroundColorSpan(colors[i % colors.size]), i, i + 1, 0)
        }
        return spannable
    }

    private fun checkTimeLimitAndNavigate(activityClass: Class<*>) {
        if (getUsageTime() >= usageLimit) {
            showTimeLimitReachedDialog()
        } else {
            startActivity(Intent(this, activityClass))
        }
    }

    private fun playWelcomeMessage() {
        val message = "Hey there! Welcome to KidzAid! Ready to start your adventure? Choose your age group!"
        tts.speak(message, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    private fun startTrackingUsage() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (getUsageTime() < usageLimit) {
                    addUsageTime(60000) // Add 1 minute
                    updateRemainingTimeUI()
                    handler.postDelayed(this, 60000) // Repeat every 1 min
                } else {
                    showTimeLimitReachedDialog()
                }
            }
        }, 60000) // Start after 1 min
    }

    private fun updateRemainingTimeUI() {
        val remainingMillis = usageLimit - getUsageTime()
        val remainingMinutes = (remainingMillis / 60000).toInt()
        val remainingSeconds = ((remainingMillis % 60000) / 1000).toInt()

        remainingTimeText.text = "Time remaining: ${remainingMinutes}:${String.format("%02d", remainingSeconds)}"
    }

    private fun getUsageTime(): Long {
        return sharedPreferences.getLong("UsageTime", 0)
    }

    private fun addUsageTime(milliseconds: Long) {
        val newUsageTime = getUsageTime() + milliseconds
        sharedPreferences.edit().putLong("UsageTime", newUsageTime).apply()
    }

    private fun showTimeLimitReachedDialog() {
        AlertDialog.Builder(this)
            .setTitle("Time Limit Reached")
            .setMessage("You have reached the daily limit of 30 minutes. Come back tomorrow!")
            .setPositiveButton("OK") { _, _ -> finishAffinity() }
            .setCancelable(false)
            .show()
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale.US
        }
    }

    override fun onDestroy() {
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        super.onDestroy()
    }
}
