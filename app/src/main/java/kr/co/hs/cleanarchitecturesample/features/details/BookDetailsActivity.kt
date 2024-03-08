package kr.co.hs.cleanarchitecturesample.features.details

import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.dispose
import coil.load
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kr.co.hs.cleanarchitecturesample.R
import kr.co.hs.cleanarchitecturesample.databinding.ActivityBookDetailsBinding
import kr.co.hs.cleanarchitecturesample.domain.entities.BookDetailEntity
import kr.co.hs.cleanarchitecturesample.platform.Activity

@AndroidEntryPoint
class BookDetailsActivity : Activity() {
    companion object {
        const val EXTRA_ISBN_13 =
            "kr.co.hs.cleanarchitecturesample.BookDetailsActivity.EXTRA_ISBN_13"
    }

    private lateinit var binding: ActivityBookDetailsBinding
    private val bookDetailsViewModel: BookDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_book_details)
        binding.lifecycleOwner = this

        bookDetailsViewModel.bookDetails.observe(this) { setupUI(it) }
        bookDetailsViewModel.lastError.observe(this) { showThrowable(it) }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) { doRequestBookDetails() }
        }
    }

    private fun getISBN13() = intent?.getStringExtra(EXTRA_ISBN_13)

    fun doRequestBookDetails() {
        getISBN13()?.let { bookDetailsViewModel.request(it) }
    }

    fun setupUI(bookDetailsEntity: BookDetailEntity) {
        with(binding) {
            ivImage.load(bookDetailsEntity.imageUrl.toString()) { crossfade(true) }
            tvTitle.text = bookDetailsEntity.title
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        with(binding) {
            ivImage.dispose()
        }
    }
}