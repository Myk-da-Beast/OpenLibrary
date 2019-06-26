package com.myk.openlibrary.model


import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

// class to represent simple book results from OpenLibrary
// TODO: decide whether to use this class or Doc
open class Book(
//    @SerializedName("author_key")
//    var authorKey: List<String>,
//    @SerializedName("author_name")
//    var authorName: List<String>,
    @PrimaryKey
    @SerializedName("cover_i")
    var coverI: Int,
    @SerializedName("edition_count")
    var editionCount: Int,
    @SerializedName("first_publish_year")
    var firstPublishYear: Int,
    @SerializedName("has_fulltext")
    var hasFulltext: Boolean,
//    var ia: List<String>,
    var key: String,
    @SerializedName("public_scan_b")
    var publicScanB: Boolean,
    var title: String
) : RealmObject() {

    // default constructor for realm
    constructor() : this(
//        listOf(),
//        listOf(),
        -1,
        -1,
        -1,
        false,
//        listOf(),
        "",
        false,
        ""
    )

    val coverUrlSmall: String
        get() {
            val id = if (coverI >= 0) coverI else return ""
            return "https://covers.openlibrary.org/w/id/$id-S.jpg"
        }
    val coverUrlMedium: String
        get() {
            val id = if (coverI >= 0) coverI else return ""
            return "https://covers.openlibrary.org/w/id/$id-M.jpg"
        }
    val coverUrlLarge: String
        get() {
            val id = if (coverI >= 0) coverI else return ""
            return "https://covers.openlibrary.org/w/id/$id-L.jpg"
        }
}