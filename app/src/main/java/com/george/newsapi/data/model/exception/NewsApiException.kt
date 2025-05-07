package com.george.newsapi.data.model.exception

/**
 * News API 通用 Exception
 */
open class NewsApiException(open val status: String?, open val result: Int?) :
    Exception("${status}(${result})")