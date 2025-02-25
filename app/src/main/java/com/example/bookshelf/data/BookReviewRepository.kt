package com.example.bookshelf.data

import com.example.bookshelf.model.BookReviewResponse
import com.example.bookshelf.network.NYTApiService


interface BookReviewRepository {
    suspend fun getBookReviews(isbn: String?):BookReviewResponse
}

class NetworkReviewsRepository(
    private val nytApiService: NYTApiService
):BookReviewRepository{
    override suspend fun getBookReviews(isbn: String?): BookReviewResponse =
        nytApiService.getBookReviews(isbn)
}