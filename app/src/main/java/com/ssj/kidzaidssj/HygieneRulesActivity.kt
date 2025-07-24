package com.ssj.kidzaidssj

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class HygieneRulesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hygiene_rules)

        val buttonVideo: Button = findViewById(R.id.buttonVideo)
        val buttonQuiz: Button = findViewById(R.id.buttonQuiz)

        buttonVideo.setOnClickListener {
            startActivity(Intent(this, HygieneVideoActivity::class.java))
        }

        buttonQuiz.setOnClickListener {
            startActivity(Intent(this, HygieneQuizActivity::class.java))
        }
    }
}
