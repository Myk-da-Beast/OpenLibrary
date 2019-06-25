package com.myk.openlibrary.view

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.myk.openlibrary.R
import com.myk.openlibrary.model.Book
import com.myk.openlibrary.view.base.BaseFragment
import com.myk.openlibrary.viewModel.SearchViewModel
import io.realm.OrderedRealmCollection
import io.realm.Realm
import io.realm.RealmRecyclerViewAdapter
import kotlinx.android.synthetic.main.search_fragment.*
import kotlinx.android.synthetic.main.search_item.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class SearchFragment : BaseFragment(), SearchView.OnQueryTextListener {
    // Lazy inject ViewModel
    private val viewModel by viewModel<SearchViewModel>()
    private var adapter: RealmRecyclerViewAdapter<Book, SearchAdapter.ViewHolder>? = null
    private val realm = Realm.getDefaultInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.search_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        recycler.layoutManager = LinearLayoutManager(context)

        // observe live data
        viewModel.searchResults.observe(this, Observer {
            if (adapter == null) {
                adapter = SearchAdapter(it)
                recycler.adapter = adapter
            }
            Timber.d("Something is happening: ${it.size}")
        })
    }

    // fixme this function is called every time we switch tabs and it seems 'sticky'
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.search_menu, menu)
        val searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.setOnQueryTextListener(this)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        Timber.v("text submitted: $query")
        query?.let {
            viewModel.updateSearchQuery(query)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        Timber.v("text submitted: $newText")
        if (newText.isNullOrEmpty()) {
            adapter?.apply {
                data?.let {
                    updateData(it.where().findAll())
                }
            }
        } else {
            adapter?.apply {
                updateData(realm.where(Book::class.java).contains("title", newText).findAll())
            }
        }
        return true
    }
}

class SearchAdapter(
    collection: OrderedRealmCollection<Book>
) : RealmRecyclerViewAdapter<Book, SearchAdapter.ViewHolder>(collection, true) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.setTitle(it.title)
            holder.setCoverImage(it.coverUrl)
        }
    }

    class ViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        fun setTitle(text: String?) {
            itemView.title.text = text
        }

        fun setCoverImage(url: String) {
            Glide.with(itemView.context)
                .load(url)
//                .load("https://covers.openlibrary.org/w/id/8314541-S.jpg")
                .transition(DrawableTransitionOptions.withCrossFade())
//                .apply(RequestOptions.circleCropTransform())
//                .error(Glide.with(itemView.context).load(R.drawable.icn_pig_run))
                .into(itemView.image)
        }
    }
}
