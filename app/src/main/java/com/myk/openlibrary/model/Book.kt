package com.myk.openlibrary.model


import com.google.gson.annotations.SerializedName
import io.realm.RealmObject
import io.realm.annotations.Ignore
import io.realm.annotations.PrimaryKey

// class to represent simple book results from OpenLibrary
// TODO: decide whether to use this class or Doc
open class Book(
    @Ignore
    var isbn: List<String>,
    var title: String,
    var subtitle: String,
    @Ignore
    @SerializedName("author_name")
    var authorName: List<String>,
    @Ignore
    var subject: List<String>,
    @SerializedName("edition_count")
    var editionCount: Int,
    @Ignore
    var publisher: List<String>,
    @SerializedName("first_publish_year")
    var firstPublishYear: Int,
    @SerializedName("publish_year")
    @Ignore
    var publishYear: List<Int>,
    @SerializedName("publish_date")
    @Ignore
    var publishDate: List<String>,
    @Ignore
    var firstSentence: List<String>,
    @PrimaryKey
    @SerializedName("cover_i")
    var coverI: Int
//    @SerializedName("has_fulltext")
//    var hasFulltext: Boolean,
//    var ia: List<String>,
//    var key: String,
//    @SerializedName("public_scan_b")
//    var publicScanB: Boolean,
) : RealmObject() {
    var isOnWishList = false
    var authors = ""
    var subjects = ""
    var publishers = ""
    var preview = ""

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

    // default constructor for realm
    constructor() : this(
        listOf(), "", "", listOf(), listOf(), -1, listOf(), -1, listOf(), listOf(), listOf(), -1
    )

    fun getFirstPublishDate(): String {
        return firstPublishYear.toString()
    }
}