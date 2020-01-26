package com.alxdev.two.moneychanger.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alxdev.two.moneychanger.data.local.entity.History
import com.alxdev.two.moneychanger.databinding.ItemHistoryBinding
import com.alxdev.two.moneychanger.ui.changer.ChangerViewModel
import com.alxdev.two.moneychanger.ui.changer.HistoryItem

class IRecyclerViewAdapter(private val viewModel: ChangerViewModel) : RecyclerView.Adapter<IRecyclerViewAdapter.ViewHolder>() {
    private var items: List<History> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater =
            LayoutInflater.from(parent.context) //inflate(R.layout.item_history, parent, false)
        val itemBinding = ItemHistoryBinding.inflate(inflater, parent, false)
        return ViewHolder(itemBinding)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val history = items[position]
        val historyItem = HistoryItem(history.totalCurrencyChange())
         holder.bind(historyItem)
    }

    fun update(items: List<History>) {
        this.items = items
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val itemBinding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(history: HistoryItem) {
            itemBinding.historyItem = history
            itemBinding.viewModel = viewModel
        }
    }

}


