package com.alxdev.two.moneychanger.ui.adapters

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alxdev.two.moneychanger.data.model.History

@BindingAdapter("bind_item_data")
fun setRecycleViewDataItems(recyclerView: RecyclerView, items: List<History>?) {
    recyclerView.adapter?.takeIf { _recycleView ->
        _recycleView is IRecyclerViewAdapter
    }?.let { _recycleView ->
        items?.takeIf {
            it.isNotEmpty()
        }?.let { _historyList ->
            (_recycleView as IRecyclerViewAdapter).update(_historyList)
        }
    }
}