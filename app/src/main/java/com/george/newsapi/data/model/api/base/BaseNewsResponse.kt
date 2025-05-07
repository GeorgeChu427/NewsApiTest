package com.george.newsapi.data.model.api.base

import com.george.newsapi.data.model.exception.NewsApiException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * Api BaseResponse
 */
open class BaseNewsResponse<T> {

    val status: String? = null

    val totalResults: Int = -1

    val articles: List<T>? = null

    /**
     * 是否回應成功
     */
    open fun isSuccess(): Boolean {
        return status == API_SUCCESS_STATUS
    }

    open fun toException(): NewsApiException {
        return NewsApiException(status, totalResults)
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