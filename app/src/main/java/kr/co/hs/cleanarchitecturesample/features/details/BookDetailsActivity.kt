package kr.co.hs.cleanarchitecturesample.features.details

import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import coil.dispose
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.divider.MaterialDividerItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kr.co.hs.cleanarchitecturesample.R
import kr.co.hs.cleanarchitecturesample.databinding.ActivityBookDetailsBinding
import kr.co.hs.cleanarchitecturesample.di.NavigatorQualifier
import kr.co.hs.cleanarchitecturesample.extension.CoilExt.loadURL
import kr.co.hs.cleanarchitecturesample.extension.NumberExt.dp
import kr.co.hs.cleanarchitecturesample.navigation.Navigator
import kr.co.hs.cleanarchitecturesample.platform.Activity
import java.net.URL
import javax.inject.Inject

@AndroidEntryPoint
class BookDetailsActivity : Activity() {
    companion object {
        const val EXTRA_ISBN_13 =
            "kr.co.hs.cleanarchitecturesample.BookDetailsActivity.EXTRA_ISBN_13"
    }

    private val toolBar: MaterialToolbar by lazy { binding.toolbar }
    private val bookImageView: ImageView by lazy { binding.ivImage }
    private val recyclerViewDetails: RecyclerView by lazy { binding.recyclerViewDetails }
    private val detailItemListAdapter: BookDetailItemListAdapter
            by lazy { BookDetailItemListAdapter(::onBookDetailItemClickListener) }
    private val swipeRefreshLayout: SwipeRefreshLayout by lazy { binding.swipeRefreshLayout }

    private lateinit var binding: ActivityBookDetailsBinding
    private val bookDetailsViewModel: BookDetailsViewModel by viewModels()

    @Inject
    @NavigatorQualifier
    lateinit var navigator: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_book_details)
        binding.lifecycleOwner = this
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        recyclerViewDetails.initDetailItemList()
        swipeRefreshLayout.initRefreshLayout()

        bookDetailsViewModel.bookDetails.observe(this) { setupUI(it) }
        bookDetailsViewModel.lastError.observe(this) { showThrowable(it) }
        bookDetailsViewModel.loading.observe(this) { swipeRefreshLayout.isRefreshing = it }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) { doRequestBookDetails() }
        }
    }

    private fun getISBN13() = intent?.getStringExtra(EXTRA_ISBN_13)

    private fun doRequestBookDetails() = getISBN13()?.let { bookDetailsViewModel.request(it) }

    private fun RecyclerView.initDetailItemList() {
        layoutManager = LinearLayoutManager(context)
        adapter = detailItemListAdapter
        addItemDecoration(
            MaterialDividerItemDecoration(
                context,
                MaterialDividerItemDecoration.VERTICAL
            ).also {
                it.dividerColor = ContextCompat.getColor(context, android.R.color.transparent)
                it.dividerThickness = 2.dp.toInt()
            }
        )
    }

    private fun setupUI(items: Set<BookDetailItem>) {
        (items.find { it is BookDetailItem.ImageUrl } as? BookDetailItem.ImageUrl)
            ?.let { bookImageView.loadURL(it.url) }
        (items.find { it is BookDetailItem.Title } as? BookDetailItem.Title)
            ?.let { toolBar.title = it.value }

        detailItemListAdapter.submitList(items.filter { it !is BookDetailItem.ImageUrl })
    }

    override fun onDestroy() {
        super.onDestroy()
        with(binding) {
            ivImage.dispose()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }

    private fun onBookDetailItemClickListener(item: BookDetailItem) {
        when (item) {
            is BookDetailItem.Url -> navigator.startUrl(this, URL(item.value))
            is BookDetailItem.Preview -> navigator.startUrl(this, item.previewEntity.url)
            else -> {}
        }
    }

    /**
     * 리스트 갱신
     */
    private fun SwipeRefreshLayout.initRefreshLayout() {
        setColorSchemeColors(ContextCompat.getColor(context, R.color.purple_500))
        setOnRefreshListener {
            getISBN13()
                ?.let { bookDetailsViewModel.request(it) }
                ?: run { swipeRefreshLayout.isRefreshing = false }
        }
    }
}