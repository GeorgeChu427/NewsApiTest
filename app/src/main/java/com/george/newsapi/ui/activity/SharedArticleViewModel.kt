package com.george.newsapi.ui.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.george.newsapi.data.model.api.article.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedArticleViewModel @Inject constructor() : ViewModel() {

    private val _selectedArticle = MutableStateFlow<Article?>(null)
    val selectedArticle: StateFlow<Article?> = _selectedArticle.asStateFlow()

    fun selectArticle(article: Article) {
        _selectedArticle.value = article
    }

    fun clear() {
        viewModelScope.launch {
            delay(500L)
            _selectedArticle.value = null
        }
    }
}