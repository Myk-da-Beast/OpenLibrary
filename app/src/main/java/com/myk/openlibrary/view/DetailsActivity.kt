package com.myk.openlibrary.view

import android.graphics.drawable.Drawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.myk.openlibrary.R
import com.myk.openlibrary.databinding.ActivityDetailsBinding
import com.myk.openlibrary.model.Book
import com.myk.openlibrary.viewModel.BookViewModel
import kotlinx.android.synthetic.main.activity_details.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailsActivity : AppCompatActivity() {

    private val viewModel by viewModel<BookViewModel>()
    private lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_details)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // to provide a smooth experience we want to prevent the transition
        // animation from starting until after the view is ready (image loading)
        supportPostponeEnterTransition()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            title_text_view.transitionName = intent.getStringExtra(EXTRA_TEXT_TRANSITION_NAME)
            image.transitionName = intent.getStringExtra(EXTRA_IMAGE_TRANSITION_NAME)
        }
        viewModel.setBook(intent.getIntExtra(EXTRA_BOOK_ID, -1))
        viewModel.book.observe(this, Observer {
            actionBar?.title = it.title
            setImage(it.coverUrlMedium)
            hideEmptyFields(it)
        })

        toolbar.setNavigationOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finishAfterTransition()
            } else {
                onBackPressed()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setImage(url: String) {
        Glide.with(applicationContext)
            .load(url)
            .listener(object : RequestListener<Drawable> {
                // once one of these callbacks is called we can continue the
                // scene transition
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    supportStartPostponedEnterTransition()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: com.bumptech.glide.load.DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    supportStartPostponedEnterTransition()
                    return false
                }
            })
            .into(image)
    }

    // fixme for some reason this breaks the constraints but if it worked it would be able to hide the empty fields
    private fun hideEmptyFields(book: Book) {
//        title_label.visibleOrGone = book.title.isNotEmpty()
//        title_text_view.visibleOrGone = book.title.isNotEmpty()
//        subtitle_label.visibleOrGone = book.subtitle.isNotEmpty()
//        subtitle_text_view.visibleOrGone = book.subtitle.isNotEmpty()
//        author_label.visibleOrGone = book.authorName.isNotEmpty()
//        author_text_view.visibleOrGone = book.authorName.isNotEmpty()
//        subject_label.visibleOrGone = book.subjects.isNotEmpty()
//        subject_text_view.visibleOrGone = book.subjects.isNotEmpty()
//        publisher_label.visibleOrGone = book.publishers.isNotEmpty()
//        publisher_text_view.visibleOrGone = book.publishers.isNotEmpty()
//        publish_date_label.visibleOrGone = book.firstPublishYear >= 0
//        publish_date_text_view.visibleOrGone = book.firstPublishYear >= 0
//        preview_label.visibleOrGone = book.preview.isNotEmpty()
//        preview_text_view.visibleOrGone = book.preview.isNotEmpty()
    }

    companion object {
        const val EXTRA_BOOK_ID = "extraBookId"
        const val EXTRA_IMAGE_TRANSITION_NAME = "extraImageTransitionName"
        const val EXTRA_TEXT_TRANSITION_NAME = "extraTextTransitionName"
    }
}
