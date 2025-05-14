package com.george.newsapi.ext.date

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * 時間格式的字串轉換
 */
fun String?.dateFormat(
    inputPattern: String = "yyyy-MM-dd'T'HH:mm:ss'Z'",
    outputPattern: String = "yyyy/MM/dd",
    locale: Locale = Locale.getDefault()
): String {
    this ?: return ""
    val inputFormat = SimpleDateFormat(inputPattern, locale)
    inputFormat.timeZone = TimeZone.getTimeZone("UTC")
    val outputFormat = SimpleDateFormat(outputPattern, locale)
    return try {
        val date: Date? = inputFormat.parse(this)
        if (date != null) {
            outputFormat.format(date)
        } else {
            ""
        }
    } catch (e: Exception) {
        ""
    }
}