package com.myk.openlibrary.view

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.myk.openlibrary.R
import com.myk.openlibrary.visibleOrGone
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tabs.setupWithViewPager(pager)
        pager.adapter = Adapter(supportFragmentManager, this)
        progress.visibleOrGone = false
    }

    fun showProgress(show: Boolean) {
        if (show) {
            // we don't want to show the progress bar at all if the query is very fast, so we use this animation here
            // to delay it a bit
            progress.animate().alpha(1.0f)
        } else {
            progress.alpha = 0.0f
        }
    }

    class Adapter(
        fragmentManager: FragmentManager,
        private val context: Context
    ) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getPageTitle(position: Int): CharSequence? = if (position == 0) context.getString(R.string.search_title) else context.getString(
                    R.string.wish_list_title)

        override fun getItem(position: Int): Fragment = if (position == 0) SearchFragment() else WishListFragment()

        override fun getCount(): Int = 2
    }
}