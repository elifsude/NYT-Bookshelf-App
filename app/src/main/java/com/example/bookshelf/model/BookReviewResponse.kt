package com.example.bookshelf.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookReviewResponse(
    val status: String,
    val copyright: String,
    @SerialName("num_results") val numResults:Int,
    val results: List<BookReview>
)

@Serializable
data class BookReview(
    val url: String,
    @SerialName("publication_dt") val publicationDate: String,
    @SerialName("byline") val reviewAuthor: String,
    @SerialName("book_title") val bookTitle : String,
    @SerialName("book_author") val bookAuthor : String?,
    val summary: String,
    val uuid: String,
    val uri: String,
    val isbn13: List<String>
)
