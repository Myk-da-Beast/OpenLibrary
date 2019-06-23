package com.myk.openlibrary.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.myk.openlibrary.R
import com.myk.openlibrary.repository.BookRepository
import com.myk.openlibrary.viewModel.SearchViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class SearchFragment : Fragment() {

    // Lazy inject ViewModel
    private val viewModel by viewModel<SearchViewModel>()
    private val repository by inject<BookRepository>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.search_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel

        // observe live data
        repository.searchResults.observe(this, Observer {
            Timber.d("Something is happening: ${it.size}")
        })

        GlobalScope.launch(Dispatchers.Main) {
            repository.searchLibrary("the lord of the rings", 1)
        }
    }
}
