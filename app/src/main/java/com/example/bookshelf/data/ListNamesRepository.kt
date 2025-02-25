package com.example.bookshelf.data

import com.example.bookshelf.model.BestSellerResponse
import com.example.bookshelf.network.NYTApiService

interface ListNamesRepository {
    suspend fun getListNames(): BestSellerResponse
}

class NetworkListNamesRepository(
    private val nytApiService: NYTApiService
):ListNamesRepository{
    override suspend fun getListNames(): BestSellerResponse= nytApiService.getListNames()
}