package com.example.bookshelf.network

import com.example.bookshelf.model.BestSellerResponse
import com.example.bookshelf.model.BookReviewResponse
import com.example.bookshelf.model.ListContentResponse
import com.example.bookshelf.utils.ApiConstants
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NYTApiService {
    @GET("lists/names.json")
    suspend fun getListNames(
        @Query("api-key") apiKey:String = ApiConstants.API_KEY
    ): BestSellerResponse

    @GET("lists/{list_name_encoded}.json")
    suspend fun getListContent(
        @Path("list_name_encoded") listNameEncoded: String, // Liste adı parametre olarak alınır
        @Query("api-key") apiKey: String = ApiConstants.API_KEY
    ): ListContentResponse

    @GET("reviews.json")
    suspend fun getBookReviews(
        @Query("isbn") isbn: String?,  // ISBN 13
        @Query("api-key") apiKey: String = ApiConstants.API_KEY
    ): BookReviewResponse
}