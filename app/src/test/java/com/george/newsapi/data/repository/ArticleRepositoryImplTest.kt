package com.george.newsapi.data.repository

import com.george.newsapi.data.model.api.article.Article
import com.george.newsapi.data.model.api.base.BaseNewsResponse
import com.george.newsapi.data.model.api.base.BaseNewsResponse.Companion.API_SUCCESS_STATUS
import com.george.newsapi.data.model.api.queryparams.GetTopHeadlinesParams
import com.george.newsapi.data.model.api.queryparams.SearchNewsParams
import com.george.newsapi.data.service.NewsApiService
import com.george.newsapi.rule.MainCoroutineRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ArticleRepositoryImplTest {

    @get:Rule
    val coroutineRule = MainCoroutineRule()

    private val apiService: NewsApiService = mockk()
    private val dispatcher = coroutineRule.testDispatcher

    private lateinit var repository: ArticleRepositoryImpl

    @Before
    fun setUp() {
        repository = ArticleRepositoryImpl(apiService, dispatcher)
    }


    @Test
    fun `getTopHeadlinesArticles returns list of articles`() = runTest {
        // Arrange
        val params = mockk<GetTopHeadlinesParams>()
        val queryMap = mapOf("country" to "us")
        val fakeArticles = listOf(Article(title = "Title1", ), Article(title = "Title2"))
        val response = BaseNewsResponse<Article>(
            status = API_SUCCESS_STATUS,
            articles = fakeArticles
        )

        every { params.toQueryMap() } returns queryMap
        coEvery { apiService.getTopHeadlines(queryMap) } returns response

        // Act
        val result = repository.getTopHeadlinesArticles(params).toList()

        // Assert
        assertEquals(listOf(fakeArticles), result)
    }

    @Test
    fun `searchNews returns list of articles`() = runTest {
        // Arrange
        val params = mockk<SearchNewsParams>()
        val queryMap = mapOf("q" to "android")
        val fakeArticles = listOf(Article(title = "News1"), Article(title = "News2"))
        val response = BaseNewsResponse<Article>(
            status = API_SUCCESS_STATUS,
            articles = fakeArticles
        )

        every { params.toQueryMap() } returns queryMap
        coEvery { apiService.searchNews(queryMap) } returns response

        // Act
        val result = repository.searchNews(params).toList()

        // Assert
        assertEquals(listOf(fakeArticles), result)
    }

}