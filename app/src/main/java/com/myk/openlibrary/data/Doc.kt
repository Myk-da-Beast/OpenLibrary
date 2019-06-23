package com.myk.openlibrary.data


import com.google.gson.annotations.SerializedName

// class to represent complex book results from OpenLibrary
// TODO: decide whether to use this class or Doc
data class Doc(
    @SerializedName("title_suggest")
    val titleSuggest: String,
    @SerializedName("edition_key")
    val editionKey: List<String>,
    @SerializedName("cover_i")
    val coverI: Int,
    val isbn: List<String>,
    @SerializedName("has_fulltext")
    val hasFulltext: Boolean,
    @SerializedName("id_depósito_legal")
    val idDepósitoLegal: List<String>,
    val text: List<String>,
    @SerializedName("author_name")
    val authorName: List<String>,
    val contributor: List<String>,
    @SerializedName("ia_loaded_id")
    val iaLoadedId: List<String>,
    val seed: List<String>,
    val oclc: List<String>,
    @SerializedName("id_google")
    val idGoogle: List<String>,
    val ia: List<String>,
    @SerializedName("author_key")
    val authorKey: List<String>,
    val subject: List<String>,
    val title: String,
    @SerializedName("lending_identifier_s")
    val lendingIdentifierS: String,
    @SerializedName("ia_collection_s")
    val iaCollectionS: String,
    @SerializedName("first_publish_year")
    val firstPublishYear: Int,
    val type: String,
    @SerializedName("ebook_count_i")
    val ebookCountI: Int,
    @SerializedName("publish_place")
    val publishPlace: List<String>,
    @SerializedName("ia_box_id")
    val iaBoxId: List<String>,
    @SerializedName("edition_count")
    val editionCount: Int,
    val key: String,
    @SerializedName("id_alibris_id")
    val idAlibrisId: List<String>,
    @SerializedName("id_goodreads")
    val idGoodreads: List<String>,
    @SerializedName("author_alternative_name")
    val authorAlternativeName: List<String>,
    @SerializedName("public_scan_b")
    val publicScanB: Boolean,
    @SerializedName("id_overdrive")
    val idOverdrive: List<String>,
    val publisher: List<String>,
    @SerializedName("id_amazon")
    val idAmazon: List<String>,
    @SerializedName("id_paperback_swap")
    val idPaperbackSwap: List<String>,
    @SerializedName("id_canadian_national_library_archive")
    val idCanadianNationalLibraryArchive: List<String>,
    val language: List<String>,
    val lccn: List<String>,
    @SerializedName("last_modified_i")
    val lastModifiedI: Int,
    @SerializedName("lending_edition_s")
    val lendingEditionS: String,
    @SerializedName("id_librarything")
    val idLibrarything: List<String>,
    @SerializedName("cover_edition_key")
    val coverEditionKey: String,
    val person: List<String>,
    @SerializedName("publish_year")
    val publishYear: List<Int>,
    @SerializedName("printdisabled_s")
    val printdisabledS: String,
    val place: List<String>,
    val time: List<String>,
    @SerializedName("publish_date")
    val publishDate: List<String>,
    @SerializedName("id_wikidata")
    val idWikidata: List<String>,
    @SerializedName("first_sentence")
    val firstSentence: List<String>,
    val subtitle: String,
    @SerializedName("id_amazon_ca_asin")
    val idAmazonCaAsin: List<String>,
    @SerializedName("id_amazon_it_asin")
    val idAmazonItAsin: List<String>,
    @SerializedName("id_amazon_co_uk_asin")
    val idAmazonCoUkAsin: List<String>,
    @SerializedName("id_amazon_de_asin")
    val idAmazonDeAsin: List<String>,
    @SerializedName("id_nla")
    val idNla: List<String>,
    @SerializedName("id_bcid")
    val idBcid: List<String>,
    @SerializedName("id_british_national_bibliography")
    val idBritishNationalBibliography: List<String>
)