package com.myk.openlibrary.view

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.myk.openlibrary.databinding.SearchItemBinding
import com.myk.openlibrary.model.Book
import com.myk.openlibrary.view.base.BaseFragment
import com.myk.openlibrary.viewModel.SearchViewModel
import io.realm.OrderedRealmCollection
import io.realm.Realm
import io.realm.RealmRecyclerViewAdapter
import kotlinx.android.synthetic.main.search_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import com.myk.openlibrary.R
import com.myk.openlibrary.launchActivity
import com.myk.openlibrary.view.DetailsActivity.Companion.EXTRA_TRANSITION_NAME
import io.realm.Case
import java.util.*
import kotlin.concurrent.schedule


class SearchFragment : BaseFragment(), SearchView.OnQueryTextListener {
    // Lazy inject ViewModel
    private val viewModel by viewModel<SearchViewModel>()
    private var adapter: SearchAdapter? = null
    private val realm = Realm.getDefaultInstance()
    private var queryTimer: Timer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.search_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        recycler.layoutManager = LinearLayoutManager(context)

        adapter = SearchAdapter(realm.where(Book::class.java).findAll())
        recycler.adapter = adapter
        adapter?.apply {
            onItemClickListener = { _, book, view ->
                Timber.d("Clicked book: ${book.title}")

                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    requireActivity(),
                    view,
                    ViewCompat.getTransitionName(view) ?: ""
                )

                launchActivity<DetailsActivity> {
                    putInt(DetailsActivity.EXTRA_BOOK_ID, book.coverI)
                    putString(EXTRA_TRANSITION_NAME, "${book.coverI}-title")
                    options.toBundle()
                }
            }
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
            queryTimer?.cancel()
            queryTimer?.purge()
            queryTimer = Timer()
            queryTimer?.schedule(1000L) {
                Timber.d("querying API: $newText")
                viewModel.updateSearchQuery(newText)
            }
        }
        return true
    }
}

class SearchAdapter(
    collection: OrderedRealmCollection<Book>
) : RealmRecyclerViewAdapter<Book, SearchAdapter.ViewHolder>(collection, true) {

    // npasses the adapter position, book object that was clicked, and view(s)
    // that will be shared in the scene transtion
    var onItemClickListener: ((Int, Book, View) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = SearchItemBinding.inflate(layoutInflater, parent, false)
        val holder = ViewHolder(binding)
        holder.itemView.setOnClickListener {
            val item = getItem(holder.adapterPosition)
            item ?: return@setOnClickListener
            onItemClickListener?.invoke(holder.adapterPosition, item, holder.textView)
        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
            // transition names must be unique
            ViewCompat.setTransitionName(holder.textView, "${it.coverI}-title")
        }
    }

    class ViewHolder(
        private val binding: SearchItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        val textView = binding.titleTextView

        fun bind(book: Book) {
            binding.title = book.title
            binding.coverImageUrl = book.coverUrlSmall
            // need to call this or else recyclerView may load data into the wrong cells
            binding.executePendingBindings()
        }
    }
}