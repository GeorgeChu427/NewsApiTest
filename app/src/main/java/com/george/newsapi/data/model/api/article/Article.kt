package com.george.newsapi.data.model.api.article

/**
 * 新聞文章 Model
 * @param source for the source this article came from.
 * @param author The author of the article
 * @param title The headline or title of the article.
 * @param description A description or snippet from the article.
 * @param url The direct URL to the article.
 * @param urlToImage The URL to a relevant image for the article.
 * @param publishedAt The date and time that the article was published, in UTC (+000)
 * @param content The unformatted content of the article, where available. This is truncated to 200 chars.
 */
data class Article(
    val source: Source? = null,
    val author: String? = null,
    val title: String? = null,
    val description: String? = null,
    val url: String? = null,
    val urlToImage: String? = null,
    val publishedAt: String? = null,
    val content: String? = null
)