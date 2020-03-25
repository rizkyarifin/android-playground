package com.sample.playground.base

import androidx.annotation.CallSuper
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.hadilq.liveevent.LiveEvent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

open class BaseViewModel : ViewModel(){

    val isLoading = ObservableField<Boolean>(false)
    val showMessage = LiveEvent<String>()
    val mCompositeDisposable = CompositeDisposable()

    fun launch(job:() -> Disposable) {
        mCompositeDisposable.add(job())
    }

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        mCompositeDisposable.clear()
    }

    fun handleError(throwable: Throwable) : String {
        if (throwable is UnknownHostException || throwable is IOException ||
            throwable is SocketTimeoutException){
            return "Internet is busy try again later."
        }else if (throwable is HttpException){
            val code = throwable.code()
            if (code == 400){

                return "Wrong"
            }else {
                return "Internet is busy try again later."
            }
        }else {
            return "Something went wrong"
        }
    }
}