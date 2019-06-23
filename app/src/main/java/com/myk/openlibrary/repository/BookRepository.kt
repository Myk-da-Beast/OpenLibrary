package com.myk.openlibrary.repository

import androidx.annotation.IntRange
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.myk.openlibrary.NoInternetException
import com.myk.openlibrary.model.OpenLibraryResponse
import com.myk.openlibrary.model.Doc
import com.myk.openlibrary.network.OpenLibraryApiService
import timber.log.Timber

interface BookRepository {
    val searchResults: LiveData<OpenLibraryResponse>
    val wishList: LiveData<Doc>

    /**
     * Queries books from the OpenLibrary.org
     *
     * @param searchString The string query to be performed, of the form 'this is a book title'
     * @param page How many results to skip in number of pages. Each page contains 100 results, so a value of 2
     *             for page would skip the first 200 results.
     */
    suspend fun searchLibrary(searchString: String, page: Int)

    suspend fun loadWishlist()
}

class BookRepositoryImpl(
    private val openLibraryApi: OpenLibraryApiService
): BookRepository {

    override val searchResults: LiveData<OpenLibraryResponse>
        get() = _searchResults
    override val wishList: LiveData<Doc>
        get() = _wishList

    private val _searchResults = MutableLiveData<OpenLibraryResponse>()
    private val _wishList = MutableLiveData<Doc>()

    override suspend fun searchLibrary(searchString: String, @IntRange(from = 1) page: Int) {
        try {
            openLibraryApi.searchForBookAsync(searchString, page).await().let {
                _searchResults.postValue(it)
            }
        } catch (error: NoInternetException) {
            Timber.e("No/low internet connectivity: $error")
        }
    }

    override suspend fun loadWishlist() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}