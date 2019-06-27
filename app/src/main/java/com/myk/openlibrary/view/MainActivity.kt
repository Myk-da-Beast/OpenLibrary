package com.myk.openlibrary.view

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.myk.openlibrary.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tabs.setupWithViewPager(pager)
        pager.adapter = Adapter(supportFragmentManager, this)
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
