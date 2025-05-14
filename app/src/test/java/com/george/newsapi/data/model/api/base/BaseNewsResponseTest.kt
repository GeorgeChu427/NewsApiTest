package com.george.newsapi.data.model.api.base

import com.george.newsapi.data.model.api.article.Article
import com.george.newsapi.data.model.exception.NewsApiException
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BaseNewsResponseTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    @Test
    fun `isSuccess should return true when status is ok`() {
        val response = BaseNewsResponse<Article>(status = "ok")
        assertTrue(response.isSuccess())
    }

    @Test
    fun `isSuccess should return false when status is error`() {
        val response = BaseNewsResponse<Article>(status = "error")
        assertFalse(response.isSuccess())
    }

    @Test
    fun `getOrThrow should return articles when isSuccess is true`() {
        val articles = listOf(Article(title = "Title 1"), Article(title = "Title 2"))
        val response = BaseNewsResponse(status = "ok", articles = articles)
        assertEquals(articles, response.getOrThrow())
    }

    @Test(expected = NewsApiException::class)
    fun `getOrThrow should throw NewsApiException when isSuccess is false`() {
        val responseWithError = BaseNewsResponse<Article>(
            status = "error",
            code = "123",
            message = "Something went wrong"
        )
        responseWithError.getOrThrow()
    }

    @Test
    fun `getOrThrow should return empty list when articles is null and isSuccess is true`() {
        val responseWithNullArticles = BaseNewsResponse<Article>(status = "ok", articles = null)
        assertEquals(emptyList<Article>(), responseWithNullArticles.getOrThrow())
    }

    @Test
    fun `callApiAsFlow should emit articles when API call is successful`() =
        runTest(testDispatcher) {
            // Arrange
            val articles = listOf(Article(title = "Title A"), Article(title = "Title B"))
            val mockResponse = BaseNewsResponse(status = "ok", articles = articles)

            val mockApi: suspend () -> BaseNewsResponse<Article> = mockk()
            coEvery { mockApi.invoke() } returns mockResponse

            // Act
            val result = callApiAsFlow(dispatcher = testDispatcher, api = mockApi).first()
            assertEquals(articles, result)
        }

    @Test
    fun `callApiAsFlow should throw NewsApiException when API call returns an error`() =
        runTest(testDispatcher) {
            // Arrange
            val errorCode = "503"
            val errorMessage = "Service Unavailable"
            val mockApi: suspend () -> BaseNewsResponse<Article> = mockk()
            coEvery { mockApi.invoke() } returns BaseNewsResponse(
                status = "error",
                code = errorCode,
                message = errorMessage
            )

            // Act
            val flow = callApiAsFlow(dispatcher = testDispatcher, api = mockApi)
            flow.catch { error ->
                // Assert
                assertTrue(error is NewsApiException)
            }.collect { }
        }

    @Test
    fun `callApiAsFlow should handle API returning null articles on success`() =
        runTest(testDispatcher) {
            // Arrange
            val mockApi: suspend () -> BaseNewsResponse<Article> = mockk()
            coEvery { mockApi.invoke() } returns BaseNewsResponse(status = "ok", articles = null)

            // Act
            val result = callApiAsFlow(dispatcher = testDispatcher, api = mockApi).first()

            // Assert
            assertEquals(emptyList<Article>(), result)
        }
}