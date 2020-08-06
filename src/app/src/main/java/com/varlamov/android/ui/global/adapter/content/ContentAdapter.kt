package com.varlamov.android.ui.global.adapter.content

import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.varlamov.android.ui.global.adapter.DummyDiffItemCallback

class ContentAdapter(
    itemDiff: (old: Any, new: Any) -> Boolean,
    vararg delegate: AdapterDelegate<MutableList<Any>>
) : AsyncListDifferDelegationAdapter<Any>(DummyDiffItemCallback(itemDiff)) {

    init {
        items = mutableListOf()
        delegate.forEach { delegatesManager.addDelegate(it) }
    }

    fun update(data: List<Any>) {
        items = data.toMutableList()
    }
}
