package com.myk.openlibrary.repository

import com.myk.openlibrary.NoInternetException
import com.myk.openlibrary.database.Database
import com.myk.openlibrary.model.Book
import com.myk.openlibrary.network.OpenLibraryApiService
import io.realm.RealmQuery
import timber.log.Timber
import java.io.IOException

interface BookRepository {
    /**
     * Queries books from the OpenLibrary.org
     *
     * @param searchString The string query to be performed
     * @param page How many results to skip in number of pages. Each page contains 100 results, so a value of 2
     *             for page would skip the first 200 results.
     */
    suspend fun searchLibrary(searchString: String, page: Int)

    suspend fun getBook(id: Int): Book?

    suspend fun cacheBook(book: Book)

    fun getAllBooksQuery(): RealmQuery<Book>

    fun getWishListQuery(): RealmQuery<Book>
}

class BookRepositoryImpl(
    private val openLibraryApi: OpenLibraryApiService,
    private val database: Database
): BookRepository {

    override suspend fun searchLibrary(searchString: String, page: Int) {
        try {
            // query results from internet and cache them
            openLibraryApi.searchForBookAsync(searchString, page).await().let {
                database.cacheBooks(it.docs, true)
            }
        } catch (error: NoInternetException) {
            Timber.e("No/low internet connectivity: $error")
            //TODO present error
        } catch (error: IOException) {
            Timber.e("Error: $error")
            //TODO present error
        }
    }

    override suspend fun getBook(id: Int): Book? = database.getBook(id)

    override suspend fun cacheBook(book: Book) {
        database.cacheBooks(listOf(book), true)

        // we want to persist wishlisted items so that they will be available across
        // app sessions
        if (book.isOnWishList) {
            database.cacheBooks(listOf(book), false)
        }
    }

    override fun getAllBooksQuery(): RealmQuery<Book> = database.getAllBooksQuery()

    override fun getWishListQuery(): RealmQuery<Book> = database.getWishListQuery()
}