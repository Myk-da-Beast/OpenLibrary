package com.myk.openlibrary.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.myk.openlibrary.R
import com.myk.openlibrary.databinding.ActivityDetailsBinding
import com.myk.openlibrary.model.Book
import com.myk.openlibrary.viewModel.BookViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class DetailsActivity : AppCompatActivity() {

    private val viewModel by viewModel<BookViewModel>()
    private lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_details)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_details)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

//        supportPostponeEnterTransition()

//        viewModel.book.observeForever {
//            Timber.d("adsf: ${it.title}")
//            Timber.d("??? ${viewModel.book.value?.title}")
//            Timber.d("!!! ${binding.viewModel?.book?.value?.title}")
//            binding.title.text = it.title
//        }
        val transitionName = intent.getStringExtra(EXTRA_TRANSITION_NAME)
        viewModel.setBook(intent.getIntExtra(EXTRA_BOOK_ID, -1))
    }

    companion object {
        val EXTRA_BOOK_ID = "extraBookId"
        val EXTRA_TRANSITION_NAME = "extraTransitionName"
    }
}
