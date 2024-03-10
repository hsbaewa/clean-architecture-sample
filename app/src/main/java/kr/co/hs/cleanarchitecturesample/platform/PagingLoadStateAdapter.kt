package kr.co.hs.cleanarchitecturesample.platform

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import kr.co.hs.cleanarchitecturesample.databinding.LayoutListItemPagingLoadStateBinding

class PagingLoadStateAdapter : LoadStateAdapter<PagingLoadStateViewHolder>() {
    override fun onBindViewHolder(holder: PagingLoadStateViewHolder, loadState: LoadState) {}

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): PagingLoadStateViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = LayoutListItemPagingLoadStateBinding.inflate(inflater, parent, false)
        return PagingLoadStateViewHolder(binding)
    }
}