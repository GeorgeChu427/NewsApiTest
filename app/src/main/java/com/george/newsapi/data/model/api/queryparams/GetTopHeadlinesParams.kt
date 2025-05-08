package com.george.newsapi.data.model.api.queryparams

/**
 * 取得熱門新聞參數
 */
data class GetTopHeadlinesParams(
    val country: String,
    val category: String? = null,
    val sources: String? = null,
    val pageSize: Int = 20,
    val page: Int = 1
) {

    fun toQueryMap(): Map<String, String> {
        val map = mutableMapOf<String, String>()
        map["country"] = country
        category?.let { map["category"] = category }
        sources?.let { map["sources"] = sources }
        map["pageSize"] = pageSize.toString()
        map["page"] = page.toString()
        return map
    }
}
