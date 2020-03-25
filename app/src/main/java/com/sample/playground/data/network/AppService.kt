package com.sample.playground.data.network

import com.sample.playground.data.model.NewsResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface AppService {

    @GET("everything?q=sports&apiKey=aa67d8d98c8e4ad1b4f16dbd5f3be348")
    fun getNews(@Query("page") page: Int, @Query("pageSize") pageSize: Int): Single<NewsResponse>
}
