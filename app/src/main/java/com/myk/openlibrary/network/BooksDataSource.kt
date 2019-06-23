package com.myk.openlibrary.network

import androidx.annotation.IntRange
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.myk.openlibrary.NoInternetException
import com.myk.openlibrary.data.BooksResponse
import timber.log.Timber

interface BooksDataSource {
    val queriedBooks: LiveData<BooksResponse>

    /**
     * Queries books from the OpenLibrary.org
     *
     * @param query The string query to be performed, of the form 'this is a query'
     * @param page How many results to skip in number of pages. Each page contains 100 results, so a value of 2
     *             for page would skip the first 200 results.
     */
    suspend fun queryBooks(query: String, page: Int)
}

class BooksDataSourceImpl(private val openLibraryApi: OpenLibraryApi) : BooksDataSource {

    override val queriedBooks: LiveData<BooksResponse>
        get() = _queriedBooks
    private val _queriedBooks = MutableLiveData<BooksResponse>()

    override suspend fun queryBooks(query: String, @IntRange(from = 1) page: Int) {
        try {
            openLibraryApi.searchForBookAsync(query, page).await().let {
                _queriedBooks.postValue(it)
            }
        } catch (error: NoInternetException) {
            Timber.e("No/low internet connectivity: $error")
        }
    }
}