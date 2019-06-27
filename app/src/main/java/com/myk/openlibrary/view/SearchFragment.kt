package com.myk.openlibrary.view

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.myk.openlibrary.model.Book
import com.myk.openlibrary.view.base.BaseFragment
import com.myk.openlibrary.viewModel.SearchViewModel
import io.realm.Realm
import kotlinx.android.synthetic.main.search_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import com.myk.openlibrary.databinding.SearchFragmentBinding
import com.myk.openlibrary.view.DetailsActivity.Companion.EXTRA_BOOK_ID
import com.myk.openlibrary.view.DetailsActivity.Companion.EXTRA_IMAGE_TRANSITION_NAME
import com.myk.openlibrary.view.DetailsActivity.Companion.EXTRA_TEXT_TRANSITION_NAME
import com.myk.openlibrary.view.base.BookAdapter

class SearchFragment : BaseFragment() {
    // Lazy inject ViewModel
    private val viewModel by viewModel<SearchViewModel>()
    private lateinit var binding: SearchFragmentBinding
    private var adapter: BookAdapter? = null
    private val realm = Realm.getDefaultInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SearchFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = BookAdapter(realm.where(Book::class.java).findAll(), true)
        adapter?.apply {
            onItemClickListener = ::onBookClicked
            onWishListClickListener = ::onAddToWishListClicked
        }
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = adapter

        viewModel.query.observe(this, Observer {
            adapter?.updateData(it.findAll())
        })
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