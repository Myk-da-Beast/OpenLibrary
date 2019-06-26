package com.myk.openlibrary.view

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.myk.openlibrary.model.Book
import com.myk.openlibrary.view.base.BaseFragment
import com.myk.openlibrary.viewModel.SearchViewModel
import io.realm.Realm
import kotlinx.android.synthetic.main.search_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import com.myk.openlibrary.R
import com.myk.openlibrary.view.DetailsActivity.Companion.EXTRA_BOOK_ID
import com.myk.openlibrary.view.DetailsActivity.Companion.EXTRA_IMAGE_TRANSITION_NAME
import com.myk.openlibrary.view.DetailsActivity.Companion.EXTRA_TEXT_TRANSITION_NAME
import com.myk.openlibrary.view.base.BookAdapter
import io.realm.Case
import java.util.*
import kotlin.concurrent.schedule


class SearchFragment : BaseFragment(), SearchView.OnQueryTextListener {
    // Lazy inject ViewModel
    private val viewModel by viewModel<SearchViewModel>()
    private var adapter: BookAdapter? = null
    private val realm = Realm.getDefaultInstance()
    private var queryTimer: Timer = Timer()
    private val queryDelay = 500L // how long (in milliseconds) to wait before querying the API after receiving input

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.search_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        recycler.layoutManager = LinearLayoutManager(context)

        adapter = BookAdapter(realm.where(Book::class.java).findAll(), true)
        recycler.adapter = adapter
        adapter?.apply {
            onItemClickListener = ::onBookClicked
            onWishListClickListener = ::onAddToWishListClicked
        }
    }

    // fixme this function is called every time we switch tabs and it seems 'sticky'
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu, menu)
        val searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.setOnQueryTextListener(this)
    }

    override fun onQueryTextSubmit(query: String?): Boolean = false

    override fun onQueryTextChange(newText: String?): Boolean {
        Timber.v("text submitted: $newText")
        if (newText.isNullOrEmpty()) {
            // Reset the filter if the user clears the text field.
            adapter?.apply {
                data?.let {
                    updateData(it.where().findAll().sort("title"))
                }
            }
        } else {
            // We will perform the filter on the local database in case the results are cached.
            adapter?.apply {
                Timber.w("Query title contains: $newText")
                updateData(realm.where(Book::class.java).contains("title", newText, Case.INSENSITIVE).findAll())
            }
            // This timer prevents us from querying the api in the middle of the user typing up a search query. This
            // will cut down on unnecessary requests
            queryTimer.cancel()
            queryTimer.purge()
            queryTimer = Timer()
            queryTimer.schedule(queryDelay) {
                Timber.d("querying API: $newText")
                viewModel.updateSearchQuery(newText)
            }
        }
        return true
    }

    private fun onBookClicked(position: Int, book: Book, imageView: View, textView: View) {
        Timber.d("Clicked book: ${book.title}")

        activity?.let {
            ActivityOptionsCompat.makeSceneTransitionAnimation(it)
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                it,
                Pair(imageView, ViewCompat.getTransitionName(imageView) ?: "0"),
                Pair(textView, ViewCompat.getTransitionName(textView) ?: "1")
            )
            val intent = Intent(activity, DetailsActivity::class.java)
            intent.putExtra(EXTRA_BOOK_ID, book.coverI)
            intent.putExtra(EXTRA_IMAGE_TRANSITION_NAME, ViewCompat.getTransitionName(imageView))
            intent.putExtra(EXTRA_TEXT_TRANSITION_NAME, ViewCompat.getTransitionName(textView))
            startActivity(intent, options.toBundle())
        }
    }

    private fun onAddToWishListClicked(book: Book, isOnWishList: Boolean) {
        Timber.d("Clicked wishlist")

        // convert to unmanaged realm object so that we can modify it
        val unmanagedBook = realm.copyFromRealm(book)
        unmanagedBook.isOnWishList = isOnWishList

        // save changes to the object
        viewModel.cacheBook(unmanagedBook)
    }
}