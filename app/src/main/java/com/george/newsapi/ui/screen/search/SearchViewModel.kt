package com.george.newsapi.ui.screen.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.george.newsapi.data.model.api.article.Article
import com.george.newsapi.data.repository.ArticleRepository
import com.george.newsapi.di.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val articleRepository: ArticleRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _input = MutableStateFlow("")
    val input = _input.asStateFlow()

    val searchPagingFlow: Flow<PagingData<Article>> =
        _input
            .debounce(500L)
            .distinctUntilChanged()
            .flatMapLatest { query ->
                articleRepository.getSearchPagingFlow(
                    query = query
                )
            }
            .flowOn(dispatcher)
            .cachedIn(viewModelScope)



    fun setInput(input: String) {
        viewModelScope.launch(dispatcher) {
            _input.emit(input)
        }
    }
}