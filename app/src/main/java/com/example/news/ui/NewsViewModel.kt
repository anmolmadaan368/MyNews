package com.example.news.ui

import android.app.DownloadManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.news.models.ArticlesItem
import com.example.news.models.Response
import com.example.news.repository.NewsRepository
import com.example.news.util.Resource
import kotlinx.coroutines.launch

class NewsViewModel(
    val newsRepository: NewsRepository
):ViewModel() {

    val breakingNews: MutableLiveData<Resource<Response>> = MutableLiveData()
    var breakingNewsPage = 1
    var breakingNewsResponse:Response?=null

    val searchNews: MutableLiveData<Resource<Response>> = MutableLiveData()
    var searchNewsPage = 1
    var searchNewsResponse:Response?=null

    init {

        getBreakingNews("in")
    }

    fun getBreakingNews(countryCode:String) = viewModelScope.launch {
        breakingNews.postValue(Resource.Loading())
        val response = newsRepository.getBreakingNews(countryCode,breakingNewsPage)
        breakingNews.postValue(handleBreakingNewsResponse(response))
    }

    fun searchNews(searchQuery:String) = viewModelScope.launch {
        searchNews.postValue(Resource.Loading())
        val response = newsRepository.searchNews(searchQuery,searchNewsPage)
        searchNews.postValue(handleSearchNewsResponse(response))
    }

    private fun handleBreakingNewsResponse(response: retrofit2.Response<Response>): Resource<Response>{
        if(response.isSuccessful){
            response.body()?.let { resultResponse->
                breakingNewsPage++
                if(breakingNewsResponse==null){
                    breakingNewsResponse=resultResponse
                }else{
                    val oldArticles = breakingNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    if (newArticles != null) {
                        oldArticles?.addAll(newArticles)
                    }
                }
                return Resource.Success(breakingNewsResponse?:resultResponse)
            }
        }

        return Resource.Error(response.message())
    }

    private fun handleSearchNewsResponse(response: retrofit2.Response<Response>): Resource<Response>{
        if(response.isSuccessful){
            response.body()?.let { resultResponse->
                searchNewsPage++
                if(searchNewsResponse==null){
                    searchNewsResponse=resultResponse
                }else{
                    val oldArticles = searchNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    if (newArticles != null) {
                        oldArticles?.addAll(newArticles)
                    }
                }
                return Resource.Success(searchNewsResponse?:resultResponse)
            }
        }

        return Resource.Error(response.message())
    }

    fun saveArticle(article:ArticlesItem) = viewModelScope.launch {
        newsRepository.upsert(article)
    }

    fun getSavedNews() = newsRepository.getSavedNews()

    fun deleteArticle(article: ArticlesItem) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }
}