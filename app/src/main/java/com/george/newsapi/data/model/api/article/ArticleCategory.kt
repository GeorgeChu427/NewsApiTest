package com.george.newsapi.data.model.api.article

enum class ArticleCategory(val value: String) {

    BUSINESS("business"),

    ENTERTAINMENT("entertainment"),

    GENERAL("general"),

    HEALTH("health"),

    SCIENCE("science"),

    SPORTS("sports"),

    TECHNOLOGY("technology"),

    ALL("");


    companion object {
        /**
         * 由字串取得對應的 NewsCategory，無法解析則回傳 [ALL]
         */
        fun fromValue(value: String?): ArticleCategory {
            return entries.find {
                it.value.equals(value, ignoreCase = true)
            } ?: ALL
        }
    }

}