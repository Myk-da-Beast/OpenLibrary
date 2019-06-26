package com.myk.openlibrary.view.base

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.realm.OrderedRealmCollection
import io.realm.RealmModel
import io.realm.RealmRecyclerViewAdapter

abstract class BaseRealmRecyclerAdapter<T : RealmModel, VH : BaseRealmRecyclerAdapter.ViewHolder>(
    data: OrderedRealmCollection<T>?,
    autoUpdate: Boolean
) : RealmRecyclerViewAdapter<T, VH>(data, autoUpdate) {

    private var onItemClickListener: ((Int, T) -> Unit)? = null

    protected abstract fun onCreateHolder(parent: ViewGroup, viewType: Int): VH

    protected abstract fun onBindHolder(holder: VH, position: Int)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val holder = onCreateHolder(parent, viewType)
        holder.itemView.setOnClickListener { onItemClick(holder.adapterPosition) }
        return holder
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        onBindHolder(holder, position)
    }

    fun setOnItemClickListener(listener: ((Int, T) -> Unit)?) {
        onItemClickListener = listener
    }

    private fun onItemClick(position: Int) {
        val item = getItem(position)
        item ?: return
        onItemClickListener?.invoke(position, item)
    }

    abstract class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}