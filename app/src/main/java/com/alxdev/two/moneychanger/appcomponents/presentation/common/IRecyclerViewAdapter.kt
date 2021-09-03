package com.alxdev.two.moneychanger.appcomponents.presentation.changer.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil.bind
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.alxdev.two.moneychanger.BR
import com.alxdev.two.moneychanger.R
import com.alxdev.two.moneychanger.domainx.model.History
import com.alxdev.two.moneychanger.appcomponents.presentation.changer.ChangerViewModel

class IRecyclerViewAdapter(private val viewModel: ChangerViewModel) :
    RecyclerView.Adapter<ViewHolder>() {
    private var items: List<History> = listOf()

    fun update(items: List<History>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inf = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history, parent, false)
        return ViewHolder(inf)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindingImpl?.apply {
            setVariable(BR.historyItem, items[position])
            setVariable(BR.viewModel, viewModel)
            executePendingBindings()
        }
    }
}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val bindingImpl: ViewDataBinding? = bind(view)
}


