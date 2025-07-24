package com.ssj.kidzaidssj

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.view.View
import android.os.Handler
import android.os.Looper
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import androidx.core.widget.NestedScrollView

class TopicActivity : AppCompatActivity() {

    private lateinit var youtubePlayerView: YouTubePlayerView
    private lateinit var contentScrollView: NestedScrollView
    private lateinit var contentTextView: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var hackButton: Button
    private lateinit var quizButton: Button
    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: CardAdapter

    private var progressValue = 0
    private var videoWatched = false
    private var contentRead = false
    private var hackCompleted = false
    private var quizCompleted = false
    private var quizQuestionsAnswered = 0
    private var totalQuizQuestions = 0
    private var youTubePlayerInstance: YouTubePlayer? = null

    private val videoIds = mapOf(
        "Fainting" to "2-Dv9P6gLYY",
        "Heart Attack" to "jP0qT6GpBVY",
        "Epileptic Seizure" to "AaBgkBEA9eM",
        "Choking" to "QtqLAS5rgGQ"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_topic)

        youtubePlayerView = findViewById(R.id.youtubePlayerView)
        lifecycle.addObserver(youtubePlayerView)
        contentScrollView = findViewById(R.id.contentScrollView)
        contentTextView = findViewById(R.id.contentTextView)
        progressBar = findViewById(R.id.progressBar)
        hackButton = findViewById(R.id.hackButton)
        quizButton = findViewById(R.id.quizButton)
        viewPager = findViewById(R.id.viewPager)

        val topic = intent.getStringExtra("TOPIC_NAME") ?: "Fainting"
        viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        viewPager.visibility = View.GONE // Hide initially

        val quizItems = getHackAndQuizItems(topic).filter { it.options != null }
        totalQuizQuestions = quizItems.size

        adapter = CardAdapter(getHackAndQuizItems(topic)) { position ->
            val item = getHackAndQuizItems(topic)[position]

            if (item.options == null) {
                if (!hackCompleted) {
                    hackCompleted = true
                    updateProgress()
                    println("DEBUG: Hack completed, progress updated.")
                }
            } else {
                // ✅ Show correct answer immediately & delay progress update for 3 sec
                Handler(Looper.getMainLooper()).postDelayed({
                    quizQuestionsAnswered++
                    println("DEBUG: Quiz answered. Total: $quizQuestionsAnswered / $totalQuizQuestions")

                    if (quizQuestionsAnswered >= totalQuizQuestions) {
                        quizCompleted = true
                        updateProgress()
                        println("DEBUG: All quiz questions answered. Progress updated.")
                    }
                }, 3000) // ⏳ Delay by 3 seconds
            }
        }
        viewPager.adapter = adapter

        setupVideo(topic)
        setupScrollListener()
        setupButtons(topic)
        setTopicContent(topic)
    }

    private fun setupVideo(topic: String) {
        val videoId = videoIds[topic] ?: "2-Dv9P6gLYY"

        youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youTubePlayerInstance = youTubePlayer
                youTubePlayer.loadVideo(videoId, 0f)

                progressBar.progress = 0
                progressBar.visibility = View.VISIBLE
            }

            override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
                if (second >= 240 && !videoWatched) {
                    videoWatched = true
                    updateProgress()
                    println("DEBUG: 4 minutes watched, progress updated.")
                }
            }
        })
    }

    private fun setupScrollListener() {
        contentScrollView.viewTreeObserver.addOnScrollChangedListener {
            val rect = Rect()
            contentTextView.getLocalVisibleRect(rect)

            val visibleHeight = rect.bottom - rect.top
            val totalHeight = contentTextView.height

            if (!contentRead && visibleHeight >= totalHeight * 0.8) {
                contentRead = true
                updateProgress()
                println("DEBUG: Content read 80%, progress updated.")
            }
        }
    }

    private fun setupButtons(topic: String) {
        hackButton.setOnClickListener {
            if (!hackCompleted) {
                hackCompleted = true
                updateProgress()
                println("DEBUG: Hack button clicked, progress updated.")
            }
            viewPager.visibility = View.VISIBLE
            viewPager.setCurrentItem(0, true)
        }

        quizButton.setOnClickListener {
            viewPager.visibility = View.VISIBLE
            viewPager.setCurrentItem(2, true)
        }
    }

    private fun updateProgress() {
        var newProgress = 0

        if (videoWatched) newProgress += 25
        if (contentRead) newProgress += 25
        if (hackCompleted) newProgress += 25
        if (quizQuestionsAnswered >= totalQuizQuestions) {
            quizCompleted = true
            newProgress += 25
        }

        if (newProgress > progressValue) {
            progressValue = newProgress
            progressBar.progress = progressValue
            println("DEBUG: Progress updated to $progressValue%")

            if (progressValue == 100) {
                Handler(Looper.getMainLooper()).postDelayed({
                    showCelebrationScreen()
                }, 2000) // ⏳ 2-second delay before celebration screen appears
            }

        }
    }

    private fun showCelebrationScreen() {
        val intent = Intent(this, CelebrationActivity::class.java)
        startActivity(intent)
    }


private fun getHackAndQuizItems(topic: String): List<CardItem> {
        return when (topic) {
            "Fainting" -> listOf(
                CardItem("Hack 1", "If someone faints, lay them on a flat surface and raise their legs about 12 inches. This helps restore blood flow to the brain and speeds up recovery."),
                CardItem("Hack 2", "Do not shake or slap a fainting person. Instead, check their breathing, loosen tight clothing, and ensure they get fresh air."),
                CardItem("Quiz 1", "What is the correct first step to help someone who faints?",
                    options = listOf("Lay them down and raise their legs", "Give them water immediately", "Shake them to wake up"),
                    correctAnswerIndex = 0
                ),
                CardItem("Quiz 2", "Why is fresh air important when someone faints?",
                    options = listOf("It helps them regain consciousness", "It keeps them warm", "It prevents dehydration"),
                    correctAnswerIndex = 0
                )
            )
            "Choking" -> listOf(
                CardItem("Hack 1", "If a person is choking but can still cough, encourage them to keep coughing forcefully to dislodge the object."),
                CardItem("Hack 2", "If they are unable to cough or speak, perform the Heimlich maneuver: Stand behind them, place a fist above their navel, and apply quick, upward thrusts."),
                CardItem("Quiz 1", "What should you do if someone is choking but can still cough?",
                    options = listOf("Encourage them to keep coughing", "Perform the Heimlich maneuver", "Give them water"),
                    correctAnswerIndex = 0
                ),
                CardItem("Quiz 2", "Why should you avoid slapping a choking person on the back?",
                    options = listOf("It may push the object deeper", "It helps dislodge the object", "It increases oxygen supply"),
                    correctAnswerIndex = 0
                )
            )
            "Heart Attack" -> listOf(
                CardItem("Hack 1", "If someone shows signs of a heart attack, keep them calm and seated. Avoid letting them walk as it puts extra strain on the heart."),
                CardItem("Hack 2", "Call emergency services immediately. If available, help them take aspirin (unless allergic) to reduce blood clotting."),
                CardItem("Quiz 1", "Why should you never let a heart attack victim walk?",
                    options = listOf("It puts stress on the heart", "It helps increase blood circulation", "It reduces panic"),
                    correctAnswerIndex = 0
                ),
                CardItem("Quiz 2", "What is the first thing you should do if someone has chest pain?",
                    options = listOf("Call emergency services", "Give them water", "Massage their chest"),
                    correctAnswerIndex = 0
                )
            )
            "Epileptic Seizure" -> listOf(
                CardItem("Hack 1", "During a seizure, move any sharp or hard objects away to prevent injury, and time the duration of the seizure."),
                CardItem("Hack 2", "Do not hold the person down or try to stop their movements. Instead, turn them on their side to keep their airway clear."),
                CardItem("Quiz 1", "Why should you avoid holding someone down during a seizure?",
                    options = listOf("It can cause injuries", "It helps stop the seizure faster", "It prevents loss of consciousness"),
                    correctAnswerIndex = 0
                ),
                CardItem("Quiz 2", "What should you do immediately after the seizure stops?",
                    options = listOf("Turn them on their side", "Give them water", "Make them stand up immediately"),
                    correctAnswerIndex = 0
                )
            )
            else -> emptyList()
        }
    }

    private fun setTopicContent(topic: String) {
        val contentString: String = when (topic) {
            "Fainting" -> """
            <b>Understanding Fainting</b><br><br>
            Fainting (syncope) occurs due to a temporary drop in blood flow to the brain, often caused by dehydration, prolonged standing, or low blood pressure.<br><br>

            <b>Steps to Help Someone Who Faints:</b><br>
            1. Lay the person flat on their back in a safe place.<br>
            2. Elevate their legs about 12 inches to improve blood circulation.<br>
            3. Ensure they are breathing normally. If not, seek medical help immediately.<br>
            4. Loosen any tight clothing around their neck to facilitate airflow.<br>
            5. Allow them to rest before attempting to stand up.<br>
            6. If fainting occurs frequently, medical evaluation is necessary.<br><br>

            <b>Remedy & Medical Support:</b><br>
            - Drinking water or electrolyte-rich fluids can help if dehydration is the cause.<br>
            - If low blood pressure is a recurring issue, medications such as Fludrocortisone may be prescribed.
        """.trimIndent()
            "Choking" -> """
            <b>Understanding Choking</b><br><br>
            Choking occurs when food or an object blocks the airway, making it difficult to breathe. It can be partial (where the person can still cough) or complete (where no air passes through).<br><br>

            <b>Steps to Help a Choking Person:</b><br>
            1. If the person is coughing, encourage them to keep coughing forcefully.<br>
            2. If they cannot breathe or make sounds, perform the Heimlich maneuver:<br>
               - Stand behind them, place a fist just above their navel, and give quick, inward thrusts.<br>
            3. If they become unconscious, start CPR and call emergency services.<br><br>

            <b>Remedy & Medical Support:</b><br>
            - For mild choking, warm water or herbal teas like ginger tea can help clear the throat.<br>
            - In cases of frequent choking, a doctor may recommend speech therapy or swallowing therapy.
        """.trimIndent()

            "Heart Attack" -> """
            <b>Understanding a Heart Attack</b><br><br>
            A heart attack (myocardial infarction) happens when the blood supply to the heart muscle is blocked due to a clot, leading to chest pain, shortness of breath, and nausea.<br><br>

            <b>Steps to Help Someone Having a Heart Attack:</b><br>
            1. Call emergency services immediately.<br>
            2. Keep the person calm and seated to reduce heart strain.<br>
            3. Loosen tight clothing for better breathing.<br>
            4. If prescribed, assist in taking nitroglycerin to widen the blood vessels.<br>
            5. If unconscious, perform CPR until medical help arrives.<br><br>

            <b>Remedy & Medical Support:</b><br>
            - Aspirin (325 mg) can be given if the person is not allergic, as it helps thin the blood.<br>
            - Regular consumption of omega-3-rich foods (e.g., fish, flaxseeds) can improve heart health.
        """.trimIndent()

            "Epileptic Seizure" -> """
            <b>Understanding Epileptic Seizures</b><br><br>
            Seizures occur due to abnormal electrical activity in the brain, leading to uncontrolled movements, loss of awareness, and sometimes unconsciousness.<br><br>

            <b>Steps to Help During a Seizure:</b><br>
            1. Move any sharp or hard objects away to prevent injury.<br>
            2. Do not hold the person down or try to stop their movements.<br>
            3. Turn them onto their side to keep the airway clear.<br>
            4. Time the seizure; if it lasts more than 5 minutes, call emergency services.<br>
            5. Stay with them until they regain full awareness.<br><br>

            <b>Remedy & Medical Support:</b><br>
            - Anti-epileptic drugs (AEDs) like Levetiracetam or Valproate can help manage seizures.<br>
            - Some studies suggest that a ketogenic diet (high in fats, low in carbs) may help reduce seizure frequency.
        """.trimIndent()
            else -> "No information available for this topic."
        }
        val formattedContent: Spanned = Html.fromHtml(contentString, Html.FROM_HTML_MODE_LEGACY)
        contentTextView.text = formattedContent
    }
}
