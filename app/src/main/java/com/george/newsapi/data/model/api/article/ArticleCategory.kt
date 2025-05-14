package com.george.newsapi.data.model.api.article

import com.george.newsapi.data.model.strings.AppStrings
import com.george.newsapi.data.model.strings.Strings

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

fun ArticleCategory.displayName(appStrings: AppStrings): String {
    return when (this) {
        ArticleCategory.ALL -> appStrings.categoryAll
        ArticleCategory.BUSINESS -> appStrings.categoryBusiness
        ArticleCategory.ENTERTAINMENT -> appStrings.categoryEntertainment
        ArticleCategory.GENERAL -> appStrings.categoryGeneral
        ArticleCategory.HEALTH -> appStrings.categoryHealth
        ArticleCategory.SCIENCE -> appStrings.categoryScience
        ArticleCategory.SPORTS -> appStrings.categorySports
        ArticleCategory.TECHNOLOGY -> appStrings.categoryTechnology
    }
}