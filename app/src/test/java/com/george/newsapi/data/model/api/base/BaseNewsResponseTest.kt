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

    /**
     * hasNextPage
     */
    @Test
    fun `hasNextPage returns true when current page is less than total pages`() {
        val response = BaseNewsResponse<Any>(
            status = "ok",
            totalResults = 45
        )
        val currentPage = 2
        val pageSize = 20

        val result = response.hasNextPage(currentPage, pageSize)

        assertTrue(result)
    }

    @Test
    fun `hasNextPage returns false when current page equals total pages`() {
        val response = BaseNewsResponse<Any>(
            status = "ok",
            totalResults = 40
        )
        val currentPage = 2
        val pageSize = 20

        val result = response.hasNextPage(currentPage, pageSize)

        assertFalse(result)
    }

    @Test
    fun `hasNextPage returns false when current page greater than total pages`() {
        val response = BaseNewsResponse<Any>(
            status = "ok",
            totalResults = 30
        )
        val currentPage = 4
        val pageSize = 10

        val result = response.hasNextPage(currentPage, pageSize)

        assertFalse(result)
    }

    @Test
    fun `hasNextPage returns true when totalResults is not multiple of pageSize`() {
        val response = BaseNewsResponse<Any>(
            status = "ok",
            totalResults = 25
        )
        val currentPage = 1
        val pageSize = 20

        val result = response.hasNextPage(currentPage, pageSize)

        assertTrue(result)
    }

    @Test
    fun `hasNextPage returns false when totalResults is zero`() {
        val response = BaseNewsResponse<Any>(
            status = "ok",
            totalResults = 0
        )
        val currentPage = 1
        val pageSize = 20

        val result = response.hasNextPage(currentPage, pageSize)

        assertFalse(result)
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