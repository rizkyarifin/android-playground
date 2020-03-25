package com.sample.playground.data.network.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.sample.playground.data.model.Article
import com.sample.playground.utils.ext.with
import com.sample.playground.utils.view.State
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.schedulers.Schedulers
import com.sample.playground.data.network.AppService
import com.sample.playground.utils.rx.SchedulerProvider

class NewsRepository(
    private val appService: AppService,
    private val mCompositeDisposable: CompositeDisposable,
    private val scheduler: SchedulerProvider
) : PageKeyedDataSource<Int, Article>() {

    var state: MutableLiveData<State> = MutableLiveData()
    private var retryCompletable: Completable? = null

    fun launch(job: () -> Disposable) {
        mCompositeDisposable.add(job())
    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Article>
    ) {
        updateState(State.LOADING)
        launch {
            appService.getNews(1, params.requestedLoadSize).with(scheduler).subscribe({
                    updateState(State.DONE)
                    callback.onResult(it.articles,
                        null,
                        2
                    )},
                { err ->
                    updateState(State.ERROR)
                    setRetry(Action { loadInitial(params, callback) })
                })
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Article>) {
        updateState(State.LOADING)
        launch {
            appService.getNews(params.key, params.requestedLoadSize).with(scheduler).subscribe(
                {
                    updateState(State.DONE)
                    callback.onResult(it.articles,
                        params.key + 1
                    )},
                { err ->
                    updateState(State.ERROR)
                    setRetry(Action { loadAfter(params, callback) })
                })
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Article>) {
    }

    private fun updateState(state: State) {
        this.state.postValue(state)
    }

    fun retry() {
        if (retryCompletable != null) {
            mCompositeDisposable.add(
                retryCompletable!!
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
            )
        }
    }

    private fun setRetry(action: Action?) {
        retryCompletable = if (action == null) null else Completable.fromAction(action)
    }
}