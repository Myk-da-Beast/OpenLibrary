package com.myk.openlibrary.viewModel

import androidx.lifecycle.ViewModel
import com.myk.openlibrary.repository.BookRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repository: BookRepository
) : ViewModel() {
    // TODO: Implement the ViewModel

    fun updateSearchQuery(query: String) {
        GlobalScope.launch(Dispatchers.IO) {
            repository.searchLibrary(query, 2)
        }
    }
}
