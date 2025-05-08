package com.george.newsapi.ui.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.george.newsapi.data.model.api.queryparams.SearchNewsParams
import com.george.newsapi.data.repository.ArticleRepository
import com.george.newsapi.di.IoDispatcher
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val articleRepository: ArticleRepository,
    private val gson: Gson,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _viewState = MutableStateFlow("loading...")
    val viewState = _viewState.asStateFlow()


    fun getTopHeadlines() {
        viewModelScope.launch(dispatcher) {
            articleRepository
//                .getTopHeadlines(GetTopHeadlinesParams(country = "us")
                .searchNews(
                    SearchNewsParams(query = "BTC")
                )
                .catch { e ->
                    _viewState.value = e.toString()
                }
                .map {
                    gson.toJson(it)
                }
                .collect { message ->
                    _viewState.value = message
                }
        }
    }
}