package com.george.newsapi.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.george.newsapi.GET_ARTICLE_PAGE_SIZE
import com.george.newsapi.data.model.api.article.Article
import com.george.newsapi.data.model.api.article.ArticleCategory
import com.george.newsapi.data.model.api.base.callApiAsFlow
import com.george.newsapi.data.model.api.queryparams.GetTopHeadlinesParams
import com.george.newsapi.data.model.api.queryparams.SearchNewsParams
import com.george.newsapi.data.pagingsource.SearchPagingSource
import com.george.newsapi.data.pagingsource.TopHeadlinesPagingSource
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
        return callApiAsFlow(dispatcher) {
            apiService.getTopHeadlines(params.toQueryMap())
        }
    }

    override fun getTopHeadlinesPagingFlow(
        country: String,
        category: ArticleCategory
    ): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(
                pageSize = GET_ARTICLE_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                TopHeadlinesPagingSource(
                    apiService,
                    country,
                    category
                )
            }
        )
            .flow
    }

    override suspend fun searchNews(params: SearchNewsParams): Flow<List<Article>> {
        return callApiAsFlow(dispatcher) {
            apiService.searchNews(params.toQueryMap())
        }
    }

    override fun getSearchPagingFlow(query: String): Flow<PagingData<Article>> {
        return Pager(
            config = PagingConfig(
                pageSize = GET_ARTICLE_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                SearchPagingSource(
                    apiService,
                    query
                )
            }
        )
            .flow
    }
}