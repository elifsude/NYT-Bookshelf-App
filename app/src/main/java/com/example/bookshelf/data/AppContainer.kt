package com.example.bookshelf.data

import com.example.bookshelf.model.BestSellerResponse
import com.example.bookshelf.model.BookReviewResponse
import com.example.bookshelf.network.NYTApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val listNamesRepository : ListNamesRepository
    val listContentRepository : ListContentRepository
    val bookReviewRepository : BookReviewRepository
}

class DefaultAppContainer : AppContainer{
    private val BASE_URL = "https://api.nytimes.com/svc/books/v3/"
    private val retrofit:Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BASE_URL)
        .build()
    private val retrofitService : NYTApiService by lazy{
        retrofit.create(NYTApiService::class.java)
    }
    override val listNamesRepository : ListNamesRepository by lazy{
        NetworkListNamesRepository(retrofitService)
    }
    override val listContentRepository : ListContentRepository by lazy {
        NetworkListContentRepository(retrofitService)
    }
    override val bookReviewRepository : BookReviewRepository by lazy {
        NetworkReviewsRepository(retrofitService)
    }
}