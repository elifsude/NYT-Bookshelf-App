package com.example.bookshelf.data

import android.util.Log
import com.example.bookshelf.model.ListContentResponse
import com.example.bookshelf.network.NYTApiService

interface ListContentRepository {
    suspend fun getListContent(listNameEncoded:String):ListContentResponse
}


class NetworkListContentRepository(
    private val nytApiService: NYTApiService
):ListContentRepository{
    override suspend fun getListContent(listNameEncoded: String): ListContentResponse {
        Log.d("Bookshelf", "Fetching list for: $listNameEncoded")  // ✅ Check if correct list name is used

        val response = nytApiService.getListContent(listNameEncoded)

        Log.d("Bookshelf", "API Response: ${response.num_results} results")  // ✅ Check response count
        return response
    }
}