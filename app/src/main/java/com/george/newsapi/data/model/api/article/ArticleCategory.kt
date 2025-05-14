package com.george.newsapi.data.model.api.article

enum class ArticleCategory(val value: String) {

    ALL(""),

    BUSINESS("business"),

    ENTERTAINMENT("entertainment"),

    GENERAL("general"),

    HEALTH("health"),

    SCIENCE("science"),

    SPORTS("sports"),

    TECHNOLOGY("technology");


    companion object {
        fun fromValue(value: String?): ArticleCategory {
            return entries.find {
                it.value.equals(value, ignoreCase = true)
            } ?: ALL
        }
    }

}

fun ArticleCategory.displayName(): String {
    return name.lowercase().replaceFirstChar { it.uppercase() }
}