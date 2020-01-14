package com.alxdev.two.moneychanger.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alxdev.two.moneychanger.R
import com.alxdev.two.moneychanger.data.local.entity.History
import com.alxdev.two.moneychanger.data.toCurrencyFormat
import kotlinx.android.synthetic.main.item_history.view.*

class IRecyclerViewAdapter : RecyclerView.Adapter<IRecyclerViewAdapter.ViewHolder>() {

    private var items: List<History> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(items[position])

    fun update(items: List<History>) {
        this.items = items
        notifyDataSetChanged()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(history: History) {
            val localQuantity = history.localCurrencyQuantity.toCurrencyFormat()
            val foreignQuantity = history.foreignCurrencyQuantity.toCurrencyFormat()
            val total =
                (history.localCurrencyQuantity * history.foreignCurrencyQuantity).toCurrencyFormat()
            itemView.conversion_text.text =
                "Local country : ${history.localCountry} \r\nCurrency Quantity = $localQuantity \r\nForeign Country : ${history.foreignCountry} \r\nCurrency Quantity = $foreignQuantity \r\nConversion : $total"
        }
    }
}
