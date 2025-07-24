package com.ssj.kidzaidssj

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.MediaController
import android.widget.TextView
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import org.jetbrains.annotations.Nullable

class VideoActivity : AppCompatActivity() {
    private lateinit var videoView: VideoView
    private lateinit var thumbsUp: ImageView
    private lateinit var thumbsDown: ImageView
    private lateinit var likeCountText: TextView
    private lateinit var unlikeCountText: TextView
    private var isLiked = false
    private var isDisliked = false
    private var likeCount = 0
    private var unlikeCount = 0
    private var currentPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        videoView = findViewById(R.id.videoView)
        thumbsUp = findViewById(R.id.thumbsUp)
        thumbsDown = findViewById(R.id.thumbsDown)
        likeCountText = findViewById(R.id.likeCount)
        unlikeCountText = findViewById(R.id.unlikeCount)

        val mediaController = MediaController(this)
        videoView.setMediaController(mediaController)
        val videoUri = Uri.parse("android.resource://" + packageName + '/' + R.raw.wound_video)
        videoView.setVideoURI(videoUri)

        val sharedPreferences: SharedPreferences = getSharedPreferences("VideoPrefs", 0)
        currentPosition = sharedPreferences.getInt("lastPosition", 0)
        videoView.seekTo(currentPosition)
        videoView.start()

        thumbsUp.setOnClickListener { onThumbsUpClicked() }
        thumbsDown.setOnClickListener { onThumbsDownClicked() }

        videoView.setOnPreparedListener { mediaPlayer -> mediaPlayer.isLooping = false }

        updateLikeUnlikeCounts()
    }

    private fun onThumbsUpClicked() {
        if (!isLiked) {
            isLiked = true
            likeCount++
            if (isDisliked) {
                isDisliked = false
                unlikeCount--
            }
        } else {
            isLiked = false
            likeCount--
        }
        updateLikeUnlikeCounts()
    }

    private fun onThumbsDownClicked() {
        if (!isDisliked) {
            isDisliked = true
            unlikeCount++
            if (isLiked) {
                isLiked = false
                likeCount--
            }
        } else {
            isDisliked = false
            unlikeCount--
        }
        updateLikeUnlikeCounts()
    }

    private fun updateLikeUnlikeCounts() {
        likeCountText.text = likeCount.toString()
        unlikeCountText.text = unlikeCount.toString()
        thumbsUp.setColorFilter(
            if (isLiked) ContextCompat.getColor(this, R.color.holo_blue_light)
            else ContextCompat.getColor(this, R.color.colorGrey)
        )
        thumbsDown.setColorFilter(
            if (isDisliked) ContextCompat.getColor(this, R.color.holo_red_light)
            else ContextCompat.getColor(this, R.color.colorGrey)
        )
    }

    override fun onPause() {
        super.onPause()
        currentPosition = videoView.currentPosition
        val sharedPreferences: SharedPreferences = getSharedPreferences("VideoPrefs", 0)
        val editor = sharedPreferences.edit()
        editor.putInt("lastPosition", currentPosition)
        editor.apply()
    }

    override fun onResume() {
        super.onResume()
        val sharedPreferences: SharedPreferences = getSharedPreferences("VideoPrefs", 0)
        currentPosition = sharedPreferences.getInt("lastPosition", 0)
        videoView.seekTo(currentPosition)
        videoView.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        videoView.stopPlayback()
    }
}
