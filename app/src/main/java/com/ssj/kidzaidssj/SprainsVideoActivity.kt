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
import kotlin.properties.Delegates

class SprainsVideoActivity : AppCompatActivity() {

    private lateinit var videoView: VideoView
    private lateinit var thumbsUp: ImageView
    private lateinit var thumbsDown: ImageView
    private lateinit var likeCountText: TextView
    private lateinit var unlikeCountText: TextView

    private var isLiked by Delegates.notNull<Boolean>()
    private var isDisliked by Delegates.notNull<Boolean>()
    private var likeCount by Delegates.notNull<Int>()
    private var unlikeCount by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sprains_video)

        videoView = findViewById(R.id.videoView)
        thumbsUp = findViewById(R.id.thumbsUp)
        thumbsDown = findViewById(R.id.thumbsDown)
        likeCountText = findViewById(R.id.likeCount)
        unlikeCountText = findViewById(R.id.unlikeCount)

        val videoUri = Uri.parse("android.resource://${packageName}/${R.raw.sprain_video}")
        videoView.setVideoURI(videoUri)
        videoView.start()

        thumbsUp.setOnClickListener { onLikeClicked() }
        thumbsDown.setOnClickListener { onDislikeClicked() }
        videoView.setOnPreparedListener { mediaPlayer ->
            mediaPlayer.isLooping = true
        }
        videoView.setOnClickListener { toggleVideoPlayback() }

        updateLikeUnlikeCounts()
    }

    private fun onLikeClicked() {
        if (!isLiked) {
            isLiked = true
            likeCount += 1
            if (isDisliked) {
                isDisliked = false
                unlikeCount -= 1
            }
        } else {
            isLiked = false
            likeCount -= 1
        }
        updateLikeUnlikeCounts()
    }

    private fun onDislikeClicked() {
        if (!isDisliked) {
            isDisliked = true
            unlikeCount += 1
            if (isLiked) {
                isLiked = false
                likeCount -= 1
            }
        } else {
            isDisliked = false
            unlikeCount -= 1
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
            if (isLiked) ContextCompat.getColor(this, R.color.holo_blue_light)
            else ContextCompat.getColor(this, R.color.colorGrey)
        )
        thumbsDown.setColorFilter(
            if (isDisliked) ContextCompat.getColor(this, R.color.holo_red_light)
            else ContextCompat.getColor(this, R.color.colorGrey)
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        videoView.stopPlayback()
    }
}
