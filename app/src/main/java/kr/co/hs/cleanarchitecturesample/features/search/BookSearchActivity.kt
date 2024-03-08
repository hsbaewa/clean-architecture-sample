package kr.co.hs.cleanarchitecturesample.features.search

import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import dagger.hilt.android.AndroidEntryPoint
import kr.co.hs.cleanarchitecturesample.R
import kr.co.hs.cleanarchitecturesample.databinding.ActivityBookSearchBinding
import kr.co.hs.cleanarchitecturesample.platform.Activity
import kr.co.hs.cleanarchitecturesample.view.ListModeToggleButton

@AndroidEntryPoint
class BookSearchActivity : Activity() {

    private val bookSearchViewModel: BookSearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityBookSearchBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_book_search)
        binding.lifecycleOwner = this

        bookSearchViewModel.lastError.observe(this) { showThrowable(it) }

        binding.toggleListMode.setOnToggleListener(object : ListModeToggleButton.OnToggleListener {
            override fun onChangedMode(@ListModeToggleButton.ListMode listMode: Int) {
                // TODO : 리스트 모드 변경에 따른 리스트 타입 변경
            }
        })

    }

    fun doSearch() {}
}