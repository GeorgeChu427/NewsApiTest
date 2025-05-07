package com.george.newsapi.di

import com.george.newsapi.NEWS_API_KEY
import com.george.newsapi.NEWS_API_URL
import com.george.newsapi.data.service.NewsApiService
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
class NetworkModules {

    @Provides
    fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    fun provideOkHttpClient(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .connectTimeout(30L, TimeUnit.SECONDS)
            .readTimeout(30L, TimeUnit.SECONDS)
            .writeTimeout(30L, TimeUnit.SECONDS)
            .addLoggingInterceptor()
            .addHeaderInterceptor(
                "X-Api-Key" to NEWS_API_KEY
            )
    }

    @Provides
    fun provideNewsApiService(
        okHttpClientBuilder: OkHttpClient.Builder,
        gson: Gson
    ) =
        Retrofit.Builder()
            .baseUrl(NEWS_API_URL)
            .client(okHttpClientBuilder.build())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(NewsApiService::class.java)

}

/**
 * 加入Http Body Log
 */
fun OkHttpClient.Builder.addLoggingInterceptor() = apply {
    val interceptor = HttpLoggingInterceptor()
    interceptor.level = HttpLoggingInterceptor.Level.BODY
    addInterceptor(interceptor)
}

/**
 * 加入 Header Interceptor
 */
fun OkHttpClient.Builder.addHeaderInterceptor(vararg nameValues: Pair<String, String>) = apply {
    addInterceptor { chain ->
        val requestBuilder = chain.request().newBuilder()
        nameValues.forEach { (name, value) ->
            requestBuilder.header(name, value)
        }
        chain.proceed(requestBuilder.build())
    }
}