package com.example.bookshelf.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonIgnoreUnknownKeys

@JsonIgnoreUnknownKeys
@Serializable
data class BestSellerResponse(
    @SerialName("num_results") val numResults:Int,
    val results : List<BestSellerList>
)
@JsonIgnoreUnknownKeys
@Serializable
data class BestSellerList(
    @SerialName("list_name") val listName: String,
    @SerialName("display_name") val displayName: String,
    @SerialName("list_name_encoded") val listNameEncoded: String,
    @SerialName("oldest_published_date") val oldestPublishedDate: String,
    @SerialName("newest_published_date") val newestPublishedDate: String,
    @SerialName("updated") val updated: String
)

