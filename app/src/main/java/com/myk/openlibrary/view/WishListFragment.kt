package com.myk.openlibrary.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.myk.openlibrary.R
import com.myk.openlibrary.model.Book
import com.myk.openlibrary.view.base.BookAdapter
import com.myk.openlibrary.viewModel.WishListViewModel
import kotlinx.android.synthetic.main.search_fragment.*
import timber.log.Timber

class WishListFragment : Fragment() {

    // Lazy inject ViewModel
    private val viewModel by viewModel<WishListViewModel>()
    private var adapter: BookAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.wish_list_fragment, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = BookAdapter(viewModel.getWishlistQuery().findAll(), false)
        adapter?.onItemClickListener = ::onBookClicked
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(activity)
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
            intent.putExtra(DetailsActivity.EXTRA_BOOK_ID, book.coverI)
            intent.putExtra(DetailsActivity.EXTRA_IMAGE_TRANSITION_NAME, ViewCompat.getTransitionName(imageView))
            intent.putExtra(DetailsActivity.EXTRA_TEXT_TRANSITION_NAME, ViewCompat.getTransitionName(textView))

            startActivity(intent, options.toBundle())
        }
    }
}