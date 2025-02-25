package com.example.bookshelf.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@JsonIgnoreUnknownKeys
@Serializable
data class ListContentResponse(
    val num_results: Int,
    val results: ListDetails
)

@JsonIgnoreUnknownKeys
@Serializable
data class ListDetails(
    val list_name: String,
    val books: List<Book>
)

@JsonIgnoreUnknownKeys
@Serializable
data class Book(
    val rank: Int? = null,
    val rank_last_week: Int? = null,
    val weeks_on_list: Int? = null,
    val primary_isbn10: String? = null,
    val primary_isbn13: String? = null,
    val publisher: String? = null,
    val description: String? = null,
    val price: String? = null,
    val title: String? = null,
    val author: String? = null,
    val contributor: String? = null,
    val book_image: String? = null,
    val amazon_product_url: String? = null,
    val isbns: List<Isbn>? = emptyList(),
    val buy_links: List<BuyLink>? = emptyList()
)

@JsonIgnoreUnknownKeys
@Serializable
data class Isbn(
    val isbn10: String,
    val isbn13: String
)

@JsonIgnoreUnknownKeys
@Serializable
data class BuyLink(
    val name: String,
    val url: String
)


