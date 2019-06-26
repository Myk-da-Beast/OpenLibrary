package com.myk.openlibrary

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.*
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

// Fragment
inline fun <reified T : Any> Fragment.launchActivity(
    requestCode: Int = -1,
    argsBuilder: Bundle.() -> Unit = {}
) {
    activity?.let {
        val intent = Intent(it, T::class.java)
        intent.putExtras(Bundle().apply(argsBuilder))
        if (requestCode == -1) startActivity(intent)
        else startActivityForResult(intent, requestCode)
    }
}

// Binding Adapters
@BindingAdapter("imageUrl")
fun ImageView.setImageUrl(url: String?) {
    Glide.with(context)
        .load(url)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}

@set:BindingAdapter("visibleOrGone")
var View.visibleOrGone
    get() = visibility == VISIBLE
    set(value) {
        visibility = if (value) VISIBLE else GONE
    }

@set:BindingAdapter("visible")
var View.visible
    get() = visibility == VISIBLE
    set(value) {
        visibility = if (value) VISIBLE else INVISIBLE
    }