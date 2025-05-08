package com.george.newsapi.data.model.api.queryparams

import com.george.newsapi.data.model.api.article.SortBy


/**
 * 搜尋新聞參數
 */
data class SearchNewsParams(
    val query: String,
    val searchIn: String? = null,
    val sources: String? = null,
    val domains: String? = null,
    val excludeDomains: String? = null,
    val from: String? = null,
    val to: String? = null,
    val language: String? = null,
    val sortBy: SortBy? = null,
    val pageSize: Int = 20,
    val page: Int = 1
) {
    fun toQueryMap(): Map<String, String> {
        val queryMap = mutableMapOf<String, String>()
        queryMap["q"] = query
        searchIn?.let { queryMap["searchIn"] = it }
        sources?.let { queryMap["sources"] = it }
        domains?.let { queryMap["domains"] = it }
        excludeDomains?.let { queryMap["excludeDomains"] = it }
        from?.let { queryMap["from"] = it }
        to?.let { queryMap["to"] = it }
        language?.let { queryMap["language"] = it }
        sortBy?.let { queryMap["sortBy"] = it.toString() }
        queryMap["pageSize"] = pageSize.toString()
        queryMap["page"] = page.toString()
        return queryMap
    }
}