package com.george.newsapi.data.model.api.article

/**
 * 新聞 Model
 */
data class Article(
    val title: String?,
    val description: String?,
    val url: String?,
    val urlToImage: String?,
    val publishedAt: String?
)