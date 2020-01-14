package com.alxdev.two.moneychanger.adapters

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.alxdev.two.moneychanger.data.local.entity.History

@BindingAdapter("bind_item_data")
fun setRecycleViewDataItems(recyclerView: RecyclerView, items: List<History>?) {
    recyclerView.adapter?.takeIf { _recycleView ->
        _recycleView is IRecyclerViewAdapter
    }?.let { _recycleView ->
        items?.takeIf {
            it.isNotEmpty()
        }?.let {
            (_recycleView as IRecyclerViewAdapter).update(it)
        }
    }
}