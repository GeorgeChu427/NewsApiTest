package com.george.newsapi.data.model.store.config

enum class LanguageCode(
    val ISOCode: String,
    val displayName: String
) {
    Chinese(
        ISOCode = "zh",
        displayName = "中文"
    ),

    English(
        ISOCode = "en",
        displayName = "English"
    );


    companion object {

        fun find(code: String): LanguageCode? {
            return entries.find {
                it.ISOCode == code
            }
        }
    }
}