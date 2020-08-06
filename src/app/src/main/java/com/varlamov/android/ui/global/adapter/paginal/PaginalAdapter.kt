package com.varlamov.android.ui.global.adapter.paginal

import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.varlamov.android.ui.global.adapter.DummyDiffItemCallback

/**
 * Created by petrova_alena on 2019-12-05.
 */
class PaginalAdapter(
    private val nextPageCallback: () -> Unit,
    itemDiff: (old: Any, new: Any) -> Boolean,
    vararg delegate: AdapterDelegate<MutableList<Any>>
) : AsyncListDifferDelegationAdapter<Any>(
    DummyDiffItemCallback(
        itemDiff
    )
) {

    var fullData = false

    init {
        items = mutableListOf()

        delegatesManager.addDelegate(ProgressAdapterDelegate())
        delegate.forEach { delegatesManager.addDelegate(it) }
    }

    fun update(data: List<Any>, isPageProgress: Boolean) {
        items = mutableListOf<Any>().apply {
            addAll(data)
            if (isPageProgress) add(ProgressItem)
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any?>
    ) {
        super.onBindViewHolder(holder, position, payloads)
        if (!fullData && position >= items.size - 10) nextPageCallback.invoke()
    }
}
