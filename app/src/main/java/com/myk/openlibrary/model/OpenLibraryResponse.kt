package com.myk.openlibrary.model

import com.google.gson.annotations.SerializedName

// represents the full response from OpenLibrary which contains
// a list of results as well as some information about the query
data class OpenLibraryResponse(
    val start: Int,
    val numFound: Int,
    @SerializedName("num_found")    // some results come back with "numFound" and "num_Found". To be safe I handle both
    val numFoundAlternate: Int,
    val docs: List<Book>
)