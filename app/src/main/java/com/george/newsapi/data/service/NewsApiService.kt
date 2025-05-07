package com.george.newsapi.data.service

import com.george.newsapi.data.model.api.article.Article
import com.george.newsapi.data.model.api.base.BaseNewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {

    @GET("v2/top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String,
    ): BaseNewsResponse<Article>

}

