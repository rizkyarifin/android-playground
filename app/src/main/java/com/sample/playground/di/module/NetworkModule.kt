package com.sample.playground.di.module

import android.app.Application
import com.sample.playground.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import com.sample.playground.data.network.AppService
import sample.base.app.utils.Network

val networkModule = module {
    single { createOkHttpClient(androidApplication()) }
    single { createRetrofit(get()) }
    single { createAppService(get()) }
}

fun createOkHttpClient(context: Application): OkHttpClient {
    val httpLoggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT)
    val clientBuilder = OkHttpClient.Builder()

    if (BuildConfig.DEBUG) {
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        clientBuilder.addInterceptor(httpLoggingInterceptor)
    }
    return clientBuilder.build()
}

fun createRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
    .baseUrl(Network.BASE_URL)
    .client(okHttpClient)
    .addConverterFactory(GsonConverterFactory.create())
    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    .build()

fun createAppService(retrofit: Retrofit): AppService = retrofit.create(AppService::class.java)