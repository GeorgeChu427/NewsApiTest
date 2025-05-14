package com.george.newsapi.data.model.strings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf

val LocalAppStrings = compositionLocalOf<AppStrings> {
    error("No AppStrings provided")
}

object Strings {
    val current: AppStrings
        @Composable
        get() = LocalAppStrings.current
}

data class AppStrings(
    val titleHeadlines: String,
    val titleConfig: String,
    val categoryAll: String,
    val categoryBusiness: String,
    val categoryEntertainment: String,
    val categoryGeneral: String,
    val categoryHealth: String,
    val categoryScience: String,
    val categorySports: String,
    val categoryTechnology: String,
    val searchHint: String,
    val openWeb: String,
    val language: String,
    val theme: String,
    val themeDark: String,
    val themeLight: String,
    val themeSystem: String,
    val confirm: String,
    val cancel: String
)

val chineseStrings = AppStrings(
    titleHeadlines = "新聞頭條",
    titleConfig = "設定",
    categoryAll = "全部",
    categoryBusiness = "商業",
    categoryEntertainment = "娛樂",
    categoryGeneral = "一般",
    categoryHealth = "健康",
    categoryScience = "科學",
    categorySports = "體育",
    categoryTechnology = "科技",
    searchHint = "請輸入關鍵字",
    openWeb = "在瀏覽器中開啟",
    language = "語言",
    theme = "主題",
    themeDark = "深色",
    themeLight = "淺色",
    themeSystem = "系統預設",
    confirm = "確認",
    cancel = "取消"
)

val englishStrings = AppStrings(
    titleHeadlines = "Headlines",
    titleConfig = "Configuration",
    categoryAll = "All",
    categoryBusiness = "Business",
    categoryEntertainment = "Entertainment",
    categoryGeneral = "General",
    categoryHealth = "Health",
    categoryScience = "Science",
    categorySports = "Sports",
    categoryTechnology = "Technology",
    searchHint = "Search articles...",
    openWeb = "Open in browser",
    language = "Language",
    theme = "Theme",
    themeDark = "Dark",
    themeLight = "Light",
    themeSystem = "System default",
    confirm = "Confirm",
    cancel = "Cancel"
)