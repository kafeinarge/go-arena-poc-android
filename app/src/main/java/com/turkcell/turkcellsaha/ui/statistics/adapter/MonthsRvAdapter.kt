package com.turkcell.turkcellsaha.ui.statistics.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.turkcell.turkcellsaha.BR
import com.turkcell.turkcellsaha.R
import com.turkcell.turkcellsaha.domain.KeyValue
import com.turkcell.turkcellsaha.databinding.ItemMonthBinding

typealias OnMonthsClickListener = (KeyValue) -> Unit

class MonthsRvAdapter : RecyclerView.Adapter<MonthsRvAdapter.ViewHolder>() {

    private val itemList = mutableListOf<KeyValue>()
    var onItemClickListener: OnMonthsClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemMonthBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_month, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
        holder.itemView.findViewById<TextView>(R.id.tvMonth).setOnClickListener {
            onItemClickListener?.invoke(itemList[position])
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun appendList(list: List<KeyValue>) {
        itemList.addAll(list)
        notifyDataSetChanged()
    }

    class ViewHolder(private val itemRowBinding: ItemMonthBinding) :
        RecyclerView.ViewHolder(itemRowBinding.root) {

        fun bind(content: KeyValue) {
            itemRowBinding.setVariable(BR.viewData, content)
            itemRowBinding.executePendingBindings()

        }

    }
}