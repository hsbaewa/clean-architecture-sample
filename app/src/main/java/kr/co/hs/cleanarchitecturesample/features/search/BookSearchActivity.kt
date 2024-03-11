package kr.co.hs.cleanarchitecturesample.features.search

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withStarted
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kr.co.hs.cleanarchitecturesample.R
import kr.co.hs.cleanarchitecturesample.databinding.ActivityBookSearchBinding
import kr.co.hs.cleanarchitecturesample.di.NavigatorQualifier
import kr.co.hs.cleanarchitecturesample.domain.entities.BookSummaryEntity
import kr.co.hs.cleanarchitecturesample.extension.TextViewExt.clearSoftKeyboard
import kr.co.hs.cleanarchitecturesample.navigation.Navigator
import kr.co.hs.cleanarchitecturesample.platform.Activity
import kr.co.hs.cleanarchitecturesample.platform.PagingLoadStateAdapter
import kr.co.hs.cleanarchitecturesample.view.ListModeToggleButton
import javax.inject.Inject

@AndroidEntryPoint
class BookSearchActivity : Activity() {

    private val binding: ActivityBookSearchBinding
            by lazy { DataBindingUtil.setContentView(this, R.layout.activity_book_search) }
    private val recyclerViewSearchResult: RecyclerView by lazy { binding.recyclerView }
    private lateinit var listAdapter: BookSummaryListAdapter
    private val searchInput: EditText by lazy { binding.editQuery }
    private val listModeToggleButton: ListModeToggleButton by lazy { binding.toggleListMode }
    private val swipeRefreshLayout: SwipeRefreshLayout by lazy { binding.swipeRefreshLayout }
    private val bookSearchViewModel: BookSearchViewModel by viewModels()

    @Inject
    @NavigatorQualifier
    lateinit var navigator: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this

        searchInput.initInputQuery()
        listModeToggleButton.initListToggleButton()
        recyclerViewSearchResult.initRecyclerView()
        swipeRefreshLayout.initRefreshLayout()

        bookSearchViewModel.lastError.observe(this) { showThrowable(it) }
        lifecycleScope.launch {
            withStarted {
                launch {
                    bookSearchViewModel.data.collectLatest { listAdapter.submitData(it) }
                }
            }
        }
    }

    /**
     * 검색어 입력 필드
     */
    private fun EditText.initInputQuery() {
        setOnEditorActionListener { v, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    bookSearchViewModel.search(v.text.toString())
                    listAdapter.refresh()
                    v.clearSoftKeyboard()
                    recyclerViewSearchResult.scrollToPosition(0)
                    true
                }

                else -> false
            }
        }
    }


    /**
     * 리스트 토글 버튼
     */
    private fun ListModeToggleButton.initListToggleButton() {
        setOnToggleListener(object : ListModeToggleButton.OnToggleListener {
            override fun onChangedMode(@ListModeToggleButton.ListMode listMode: Int) {
                when (listMode) {
                    ListModeToggleButton.MODE_GRID -> with(recyclerViewSearchResult) {
                        listAdapter.viewType = BookSummaryListAdapter.VT_GRID
                        (layoutManager as GridLayoutManager).spanCount = 3
                    }

                    ListModeToggleButton.MODE_LINEAR -> with(recyclerViewSearchResult) {
                        listAdapter.viewType = BookSummaryListAdapter.VT_LINEAR
                        (layoutManager as GridLayoutManager).spanCount = 1
                    }
                }
            }
        })
    }


    /**
     * 검색결과 리스트
     */
    private fun RecyclerView.initRecyclerView() {
        layoutManager = GridLayoutManager(context, 1)
        adapter = BookSummaryListAdapter(::onItemClick)
            .also { listAdapter = it }
            .apply { addLoadStateListener() }
            .run { withLoadStateFooter(PagingLoadStateAdapter()) }
    }

    private fun onItemClick(item: BookSummaryEntity) = navigator.startDetail(this, item)

    private fun BookSummaryListAdapter.addLoadStateListener() =
        addLoadStateListener { loadState ->
            when (loadState.refresh) {
                is LoadState.NotLoading -> {
                    swipeRefreshLayout.isRefreshing = false
                }

                is LoadState.Error -> {
                    when (val t = (loadState.refresh as LoadState.Error).error) {
                        is IllegalArgumentException -> {}
                        else -> showThrowable(t)
                    }
                    swipeRefreshLayout.isRefreshing = false
                }

                LoadState.Loading -> {
                    swipeRefreshLayout.isRefreshing = true
                }
            }

        }


    /**
     * 리스트 갱신
     */
    private fun SwipeRefreshLayout.initRefreshLayout() {
        setOnRefreshListener { listAdapter.refresh() }
    }

}