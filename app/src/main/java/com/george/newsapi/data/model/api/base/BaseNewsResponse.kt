package com.george.newsapi.data.model.api.base

import com.george.newsapi.data.model.exception.NewsApiException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * Api BaseResponse
 * @param status If the request was successful or not. Options: ok, error. In the case of error a code and message property will be populated.
 * @param totalResults The total number of results available for your request. Only a limited number are shown at a time though, so use the page parameter in your requests to page through them.
 * @param articles
 * @param code 當 status 為 error 時，會出現
 * @param message 當 status 為 error 時，會出現
 */
open class BaseNewsResponse<T> {

    var status: String? = null

    var totalResults: Int = 0

    var articles: List<T>? = null

    var code: String? = null

    var message: String? = null

    constructor(
        status: String? = null,
        totalResults: Int = 0,
        articles: List<T>? = null,
        code: String? = null,
        message: String? = null
    ) {
        this.status = status
        this.totalResults = totalResults
        this.articles = articles
        this.code = code
        this.message = message
    }

    /**
     * 是否回應成功
     */
    open fun isSuccess(): Boolean {
        return status == API_SUCCESS_STATUS
    }

    open fun toException(): NewsApiException {
        return NewsApiException(code ?: "Unknown", message ?: "Unknown error.")
    }

    open fun getOrThrow(): List<T> {
        return if (isSuccess()) {
            articles ?: emptyList()
        } else {
            throw toException()
        }
    }


    companion object {
        const val API_SUCCESS_STATUS = "ok"
    }
}

/**
 * 轉換成 flow
 */
fun <T> BaseNewsResponse<T>.asFlow(
    dispatcher: CoroutineDispatcher = Dispatchers.IO
) = flow {
    emit(getOrThrow())
}
    .flowOn(dispatcher)