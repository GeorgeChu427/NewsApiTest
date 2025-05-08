package com.george.newsapi.data.model.exception

/**
 * News API 通用 Exception
 */
open class NewsApiException(open val code: String?, open val msg: String?) :
    Exception("${msg}(${code})")