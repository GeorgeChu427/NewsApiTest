package com.george.newsapi.data.model.api.article

/**
 * The order to sort the articles in
 */
enum class SortBy(val value: String) {

    /**
     * articles more closely related to q come first.
     */
    Relevancy("relevancy"),

    /**
     * articles from popular sources and publishers come first.
     */
    Popularity("popularity"),

    /**
     * newest articles come first.
     */
    PublishedAt("publishedAt");


    override fun toString(): String {
        return value
    }
}