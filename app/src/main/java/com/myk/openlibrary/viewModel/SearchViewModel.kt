package com.myk.openlibrary.viewModel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.myk.openlibrary.NoInternetException
import com.myk.openlibrary.R
import com.myk.openlibrary.model.Book
import com.myk.openlibrary.repository.BookRepository
import io.realm.Case
import io.realm.RealmQuery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.IOException
import java.util.*
import kotlin.concurrent.schedule

class SearchViewModel(
    private val context: Context,
    private val repository: BookRepository
) : ViewModel() {

    val query: LiveData<RealmQuery<Book>>
        get() = _query
    val isQuerying: LiveData<Boolean>
        get() = _isQuerying
    private var _query = MutableLiveData<RealmQuery<Book>>()
    private var _isQuerying = MutableLiveData<Boolean>()
    private var queryTimer: Timer = Timer()
    private val queryDelay = 1000L // how long (in milliseconds) to wait before querying the API after receiving input

    fun cacheBook(book: Book) {
        // TODO see if this can be refactored
        GlobalScope.launch(Dispatchers.IO) {
            repository.cacheBook(book)
        }
    }

    fun onQueryTextChange(newQuery: String?): Boolean {
        Timber.v("text changed: $newQuery")
        _isQuerying.postValue(true)
        if (newQuery.isNullOrEmpty()) {
            // Reset the filter if the query is empty.
            _query.postValue(repository.getAllBooksQuery().sort("title"))
            _isQuerying.postValue(false)
        } else {
            // apply filter to local data
            _query.postValue(repository.getAllBooksQuery().contains("title", newQuery, Case.INSENSITIVE))

            // This timer prevents us from querying the api in the middle of the user typing up a search query. This
            // will cut down on unnecessary requests
            queryTimer.cancel()
            queryTimer.purge()
            queryTimer = Timer()
            queryTimer.schedule(queryDelay) {
                Timber.d("querying API: $newQuery")
                updateSearchQuery(newQuery)
                _isQuerying.postValue(false)
            }
        }
        return true
    }

    private fun updateSearchQuery(query: String) {
        // TODO see if this can be refactored
        GlobalScope.launch(Dispatchers.IO) {
            try {
                repository.searchLibrary(query, 1)
            } catch (error: NoInternetException) {
                GlobalScope.launch(Dispatchers.Main) {
                    Toast.makeText(context, context.resources.getText(R.string.error_no_internet), Toast.LENGTH_SHORT)
                        .show()
                }
            } catch (error: IOException) {
                GlobalScope.launch(Dispatchers.Main) {
                    Toast.makeText(context, context.resources.getText(R.string.error_unknown), Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}
