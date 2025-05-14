package com.george.newsapi.data.model.api.queryparams

import com.george.newsapi.data.model.api.article.ArticleCategory

/**
 * 取得熱門新聞參數
 */
data class GetTopHeadlinesParams(
    val country: String,
    val category: ArticleCategory = ArticleCategory.ALL,
    val sources: String? = null,
    val pageSize: Int,
    val page: Int = 1
) {

    fun toQueryMap(): Map<String, String> {
        val map = mutableMapOf<String, String>()
        map["country"] = country
        category.let { map["category"] = category.value }
        sources?.let { map["sources"] = sources }
        map["pageSize"] = pageSize.toString()
        map["page"] = page.toString()
        return map
    }
}
