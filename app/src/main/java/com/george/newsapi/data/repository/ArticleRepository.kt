package com.george.newsapi.data.repository

import androidx.paging.PagingData
import com.george.newsapi.data.model.api.article.Article
import com.george.newsapi.data.model.api.article.ArticleCategory
import com.george.newsapi.data.model.api.queryparams.GetTopHeadlinesParams
import com.george.newsapi.data.model.api.queryparams.SearchNewsParams
import kotlinx.coroutines.flow.Flow


/**
 * 新聞相關 Repository
 */
interface ArticleRepository {

    /**
     * 取得熱門新聞
     */
    suspend fun getTopHeadlinesArticles(params: GetTopHeadlinesParams): Flow<List<Article>>

    /**
     * 取得熱門新聞 (paging)
     */
    fun getTopHeadlinesPagingFlow(
        country: String,
        category: ArticleCategory = ArticleCategory.ALL
    ): Flow<PagingData<Article>>

    /**
     * 搜尋新聞
     */
    suspend fun searchNews(params: SearchNewsParams): Flow<List<Article>>

    /**
     * 搜尋新聞 (paging)
     */
    fun getSearchPagingFlow(
        query: String,
    ): Flow<PagingData<Article>>
}