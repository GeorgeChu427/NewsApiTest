package com.george.newsapi.ui.screen.headlines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.george.newsapi.data.model.api.article.Article
import com.george.newsapi.data.model.api.article.ArticleCategory
import com.george.newsapi.data.repository.ArticleRepository
import com.george.newsapi.di.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HeadlinesViewModel @Inject constructor(
    private val articleRepository: ArticleRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _country = MutableStateFlow("us")

    private val _category = MutableStateFlow(ArticleCategory.ALL)
    val category = _category.asStateFlow()

    val topHeadlinesPagingFlow: Flow<PagingData<Article>> =
        combine(
            _country,
            _category
        ) { country, category ->
            country to category
        }
            .flatMapLatest { (country, category) ->
                articleRepository.getTopHeadlinesPagingFlow(
                    country = country,
                    category = category
                )
            }
            .flowOn(dispatcher)
            .cachedIn(viewModelScope)

    fun randomCategory() {
        viewModelScope.launch(dispatcher) {
            val randomCategory = ArticleCategory.entries
                .filter { it != ArticleCategory.ALL }
                .random()
            _category.emit(randomCategory)
        }
    }
}