package com.ssj.kidzaidssj

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class AgeGroup2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_age_group2)
        setupUI()
    }

    private fun setupUI() {
        findViewById<Button>(R.id.hygieneRulesButton).setOnClickListener {
            startActivity(Intent(this, HygieneRulesActivity::class.java))
        }
        findViewById<Button>(R.id.safetyRulesButton).setOnClickListener {
            startActivity(Intent(this, SafetyRulesActivity::class.java))
        }
        findViewById<Button>(R.id.nosebleedsButton).setOnClickListener {
            startActivity(Intent(this, NosebleedsActivity::class.java))
        }
        findViewById<Button>(R.id.burnsButton).setOnClickListener {
            startActivity(Intent(this, BurnsActivity::class.java))
        }
        findViewById<Button>(R.id.sprainsButton).setOnClickListener {
            startActivity(Intent(this, SprainsActivity::class.java))
        }
        findViewById<Button>(R.id.splintersButton).setOnClickListener {
            startActivity(Intent(this, SplintersActivity::class.java))
        }
        findViewById<Button>(R.id.insectBitesButton).setOnClickListener {
            startActivity(Intent(this, InsectBitesActivity::class.java))
        }
    }
}
