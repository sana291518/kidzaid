package com.ssj.kidzaidssj

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class AgeGroup1Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_age_group1)

        val buttonVideo: Button = findViewById(R.id.buttonVideo)
        val buttonQuiz: Button = findViewById(R.id.buttonQuiz)

        buttonVideo.setOnClickListener {
            startActivity(Intent(this, VideoActivity::class.java))
        }

        buttonQuiz.setOnClickListener {
            startActivity(Intent(this, QuizActivity::class.java))
        }
    }
}
