package com.george.newsapi.data.model.api.base

import com.george.newsapi.data.model.api.article.Article
import com.george.newsapi.data.model.exception.NewsApiException
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertFailsWith

@OptIn(ExperimentalCoroutinesApi::class)
class BaseNewsResponseTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    @Test
    fun `asFlow should emit articles when response is successful`() = runTest {
        // Arrange
        val fakeArticles = listOf(Article(title = "A"), Article(title = "B"))
        val response = BaseNewsResponse(
            status = "ok",
            totalResults = 2,
            articles = fakeArticles
        )

        // Act
        val result = response.asFlow(testDispatcher).toList()

        // Assert
        assertEquals(listOf(fakeArticles), result)
    }

    @Test
    fun `asFlow should throw exception when response is error`() = runTest {
        // Arrange
        val response = BaseNewsResponse<Article>(
            status = "error",
            code = "500",
            message = "Internal Server Error"
        )

        // Act
        val exception = assertFailsWith<NewsApiException> {
            response.asFlow(testDispatcher).collect{}
        }

        // Assert
        assertEquals("500", exception.code)
        assertEquals("Internal Server Error", exception.msg)
    }
}