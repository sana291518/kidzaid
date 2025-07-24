package com.ssj.kidzaidssj

import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper

class UsageManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("AppUsagePrefs", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = prefs.edit()

    companion object {
        private const val DAILY_USAGE_KEY = "daily_usage_time"  // Stores used time in millis
        private const val LAST_RESET_DATE_KEY = "last_reset_date" // Stores last reset date
        private const val USAGE_LIMIT = 30 * 60 * 1000 // 30 minutes in milliseconds
    }

    init {
        resetUsageIfNeeded()
    }

    // ✅ Get today's usage time
    fun getUsageTime(): Long {
        return prefs.getLong(DAILY_USAGE_KEY, 0)
    }

    // ✅ Increase usage time
    fun addUsageTime(timeInMillis: Long) {
        val currentUsage = getUsageTime()
        editor.putLong(DAILY_USAGE_KEY, currentUsage + timeInMillis)
        editor.apply()
    }

    // ✅ Check if the user can use the app
    fun isUsageAllowed(): Boolean {
        return getUsageTime() < USAGE_LIMIT
    }

    // ✅ Reset usage at midnight
    private fun resetUsageIfNeeded() {
        val lastResetDate = prefs.getString(LAST_RESET_DATE_KEY, "")
        val todayDate = java.text.SimpleDateFormat("yyyy-MM-dd").format(java.util.Date())

        if (lastResetDate != todayDate) {
            editor.putLong(DAILY_USAGE_KEY, 0)
            editor.putString(LAST_RESET_DATE_KEY, todayDate)
            editor.apply()
        }
    }
}
