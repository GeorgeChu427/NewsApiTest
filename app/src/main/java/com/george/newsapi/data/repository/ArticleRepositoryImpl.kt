package com.george.newsapi.data.repository

import com.george.newsapi.data.model.api.article.Article
import com.george.newsapi.data.model.api.base.asFlow
import com.george.newsapi.data.model.api.queryparams.GetTopHeadlinesParams
import com.george.newsapi.data.model.api.queryparams.SearchNewsParams
import com.george.newsapi.data.service.NewsApiService
import com.george.newsapi.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ArticleRepositoryImpl @Inject constructor(
    private val apiService: NewsApiService,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ArticleRepository {

    override suspend fun getTopHeadlinesArticles(params: GetTopHeadlinesParams): Flow<List<Article>> {
        return apiService.getTopHeadlines(params.toQueryMap()).asFlow(dispatcher)
    }

    override suspend fun searchNews(params: SearchNewsParams): Flow<List<Article>> {
        return apiService.searchNews(params.toQueryMap()).asFlow(dispatcher)
    }
}