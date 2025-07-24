package com.ssj.kidzaidssj

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class SplintersVideoActivity : AppCompatActivity() {

    private lateinit var videoView: VideoView
    private lateinit var thumbsUp: ImageView
    private lateinit var thumbsDown: ImageView
    private lateinit var likeCountText: TextView
    private lateinit var unlikeCountText: TextView

    private var isLiked = false
    private var isDisliked = false
    private var likeCount = 0
    private var unlikeCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splinters_video)

        videoView = findViewById(R.id.videoView)
        thumbsUp = findViewById(R.id.thumbsUp)
        thumbsDown = findViewById(R.id.thumbsDown)
        likeCountText = findViewById(R.id.likeCount)
        unlikeCountText = findViewById(R.id.unlikeCount)

        val videoUri = Uri.parse("android.resource://$packageName/${R.raw.splinter_video}")
        videoView.setVideoURI(videoUri)
        videoView.start()

        thumbsUp.setOnClickListener { handleLike() }
        thumbsDown.setOnClickListener { handleDislike() }

        videoView.setOnPreparedListener { it.isLooping = true }
        videoView.setOnClickListener { toggleVideoPlayback() }

        updateLikeUnlikeCounts()
    }

    private fun handleLike() {
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

    private fun handleDislike() {
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

    private fun toggleVideoPlayback() {
        if (videoView.isPlaying) {
            videoView.pause()
        } else {
            videoView.start()
        }
    }

    private fun updateLikeUnlikeCounts() {
        likeCountText.text = likeCount.toString()
        unlikeCountText.text = unlikeCount.toString()

        thumbsUp.setColorFilter(
            ContextCompat.getColor(
                this,
                if (isLiked) R.color.holo_blue_light else R.color.colorGrey
            )
        )

        thumbsDown.setColorFilter(
            ContextCompat.getColor(
                this,
                if (isDisliked) R.color.holo_red_light else R.color.colorGrey
            )
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        videoView.stopPlayback()
    }
}
