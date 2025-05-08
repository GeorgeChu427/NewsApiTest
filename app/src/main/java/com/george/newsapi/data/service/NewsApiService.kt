package com.george.newsapi.data.service

import com.george.newsapi.data.model.api.article.Article
import com.george.newsapi.data.model.api.article.SortBy
import com.george.newsapi.data.model.api.base.BaseNewsResponse
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface NewsApiService {

    /**
     * 搜尋所有新聞
     * 符合 q 關鍵字等
     * @param [SearchNewsParams]
     */
    @GET("v2/everything")
    suspend fun searchNews(
        @QueryMap params: Map<String, String>
    ): BaseNewsResponse<Article>

    /**
     * 熱門新聞
     * @param [GetTopHeadlinesParams]
     */
    @GET("v2/top-headlines")
    suspend fun getTopHeadlines(
        @QueryMap params: Map<String, String>
    ): BaseNewsResponse<Article>
}

