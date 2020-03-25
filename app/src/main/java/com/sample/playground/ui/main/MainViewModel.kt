package com.sample.playground.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.sample.playground.base.BaseViewModel
import com.sample.playground.data.model.Article
import com.sample.playground.data.network.repository.NewsRepository
import com.sample.playground.utils.rx.SchedulerProvider
import com.sample.playground.utils.view.State
import com.sample.playground.data.network.AppService

class MainViewModel(
    private val appService: AppService,
    private val scheduler: SchedulerProvider
) : BaseViewModel() {

    private val pageSize = 5
    var dataNews :LiveData<PagedList<Article>>
    val newsDataSourceLiveData = MutableLiveData<NewsRepository>()

    init {
        val config = PagedList.Config.Builder()
            .setPageSize(pageSize)
            .setInitialLoadSizeHint(pageSize * 2)
            .setEnablePlaceholders(false)
            .build()
        dataNews  = initializedPagedListBuilder(config).build()
    }

    fun getState(): LiveData<State> = Transformations.switchMap<NewsRepository,
            State>(newsDataSourceLiveData, NewsRepository::state)

    fun retry() {
        newsDataSourceLiveData.value?.retry()
    }

    fun listIsEmpty(): Boolean {
        return dataNews.value?.isEmpty() ?: true
    }

    private fun initializedPagedListBuilder(config: PagedList.Config):
            LivePagedListBuilder<Int, Article> {

        val dataSourceFactory = object : DataSource.Factory<Int, Article>() {
            override fun create(): DataSource<Int, Article> {
                val newsDataSource = NewsRepository(appService,mCompositeDisposable, scheduler)
                newsDataSourceLiveData.postValue(newsDataSource)
                return newsDataSource
            }
        }
        return LivePagedListBuilder<Int, Article>(dataSourceFactory, config)
    }
}