package com.myk.openlibrary.view.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.myk.openlibrary.databinding.SearchItemBinding
import com.myk.openlibrary.model.Book
import com.myk.openlibrary.visibleOrGone
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter

class BookAdapter(
    collection: OrderedRealmCollection<Book>,
    private val isSearch: Boolean
) : RealmRecyclerViewAdapter<Book, BookAdapter.ViewHolder>(collection, true) {

    // passes the adapter position, book object that was clicked, and view(s)
    // that will be shared in the scene transtion
    var onItemClickListener: ((Int, Book, View, View) -> Unit)? = null
    var onWishListClickListener: ((Book, Boolean) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = SearchItemBinding.inflate(layoutInflater, parent, false)
        val holder = ViewHolder(binding)

        // hide the wishlist icon if you are on the wish list page
        holder.wishListView.visibleOrGone = isSearch

        // click listener for the cell
        holder.itemView.setOnClickListener {
            val item = getItem(holder.adapterPosition)
            item ?: return@setOnClickListener
            onItemClickListener?.invoke(holder.adapterPosition, item, holder.imageView, holder.textView)
        }

        // click listener for the wish list icon
        holder.wishListView.setOnClickListener {
            val item = getItem(holder.adapterPosition)
            item ?: return@setOnClickListener
            binding.wishListIcon.isSelected = !binding.wishListIcon.isSelected
            onWishListClickListener?.invoke(item, binding.wishListIcon.isSelected)
        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)

            // transition names must be unique
            ViewCompat.setTransitionName(holder.imageView, "${it.coverI}-image")
            ViewCompat.setTransitionName(holder.textView, "${it.coverI}-title")
        }
    }

    open class ViewHolder(
        private val binding: SearchItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        val textView = binding.titleTextView
        val imageView = binding.image
        val wishListView = binding.wishListIcon

        fun bind(book: Book) {
            binding.book = book
            binding.wishListIcon.isSelected = book.isOnWishList

            // need to call this or else recyclerView may load data into the wrong cells
            binding.executePendingBindings()
        }
    }
}