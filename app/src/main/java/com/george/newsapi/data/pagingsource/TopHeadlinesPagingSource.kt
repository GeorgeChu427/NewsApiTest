package com.george.newsapi.data.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.george.newsapi.data.model.api.article.Article
import com.george.newsapi.data.model.api.article.ArticleCategory
import com.george.newsapi.data.model.api.queryparams.GetTopHeadlinesParams
import com.george.newsapi.data.service.NewsApiService

/**
 * 用於分頁載入熱門新聞的 PagingSource 實作。
 *
 * @param apiService [NewsApiService]
 * @param country 國家代碼
 * @param category 類別
 */
class TopHeadlinesPagingSource(
    private val apiService: NewsApiService,
    private val country: String,
    private val category: ArticleCategory = ArticleCategory.ALL
) : PagingSource<Int, Article>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        return try {
            val page = params.key ?: 1
            val requestParams = GetTopHeadlinesParams(
                country = country,
                category = category,
                page = page,
                pageSize = params.loadSize
            )
            val response = apiService.getTopHeadlines(
                requestParams.toQueryMap()
            )
            val articles = response.getOrThrow()

            val hasNextPage = response.hasNextPage(page, requestParams.pageSize)

            LoadResult.Page(
                data = articles,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (!hasNextPage) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(1)
        }
    }
}