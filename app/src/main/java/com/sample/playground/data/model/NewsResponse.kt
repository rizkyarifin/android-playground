package com.sample.playground.data.model

import java.io.Serializable

data class NewsResponse(val status: String,
                        val totalResults: Int,
                        val articles: List<Article>) : Serializable

data class Article(val source: Source,
                   val author: String,
                   val title: String,
                   val description: String,
                   val url: String,
                   val urlToImage: String?,
                   val publishedAt: String,
                   val content: String) : Serializable

data class Source(val id: String, val name: String) : Serializable

abstract class BaseApiResponse {
    var status: Int = 0
    var message: String? = null
}