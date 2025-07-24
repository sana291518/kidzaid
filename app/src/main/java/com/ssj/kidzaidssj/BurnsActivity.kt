package com.ssj.kidzaidssj

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class BurnsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_burns)

        findViewById<Button>(R.id.buttonVideo).setOnClickListener {
            startActivity(Intent(this, BurnsVideoActivity::class.java))
        }
        findViewById<Button>(R.id.buttonQuiz).setOnClickListener {
            startActivity(Intent(this, BurnsQuizActivity::class.java))
        }
    }
}
