package com.myk.openlibrary.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.myk.openlibrary.R
import com.myk.openlibrary.network.BooksDataSource
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    // Lazy inject ViewModel
    private val viewModel: SearchViewModel by viewModel()
    private val dataSource: BooksDataSource by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.search_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }

    companion object {
        fun newInstance() = SearchFragment()
    }
}
