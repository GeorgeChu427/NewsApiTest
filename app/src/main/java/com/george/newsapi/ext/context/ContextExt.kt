package com.george.newsapi.ext.context

import android.content.Context
import android.content.res.Configuration

/**
 * 系統目前是否是黑暗模式
 */
fun Context.isSystemInDarkTheme(): Boolean {
    return (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
}