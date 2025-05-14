package com.george.newsapi.data.pagingsource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.george.newsapi.data.model.api.article.Article
import com.george.newsapi.data.model.api.queryparams.SearchNewsParams
import com.george.newsapi.data.service.NewsApiService

class SearchPagingSource(
    private val apiService: NewsApiService,
    private val query: String
) : PagingSource<Int, Article>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        return try {
            if (query.isBlank()) {
                return LoadResult.Page(
                    data = emptyList(),
                    prevKey = null,
                    nextKey = null
                )
            }

            val page = params.key ?: 1
            val requestParams = SearchNewsParams(
                query = query,
                page = page,
                pageSize = params.loadSize
            )
            val response = apiService.searchNews(
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