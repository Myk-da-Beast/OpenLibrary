package com.myk.openlibrary.model


import com.google.gson.annotations.SerializedName

// class to represent simple book results from OpenLibrary
// TODO: decide whether to use this class or Doc
data class Book(
    @SerializedName("author_key")
    val authorKey: List<String>,
    @SerializedName("author_name")
    val authorName: List<String>,
    @SerializedName("cover_i")
    val coverI: Int,
    @SerializedName("edition_count")
    val editionCount: Int,
    @SerializedName("first_publish_year")
    val firstPublishYear: Int,
    @SerializedName("has_fulltext")
    val hasFulltext: Boolean,
    val ia: List<String>,
    val key: String,
    @SerializedName("public_scan_b")
    val publicScanB: Boolean,
    val title: String
)