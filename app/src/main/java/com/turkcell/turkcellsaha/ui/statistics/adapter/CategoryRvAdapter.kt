package com.turkcell.turkcellsaha.ui.statistics.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.turkcell.turkcellsaha.BR
import com.turkcell.turkcellsaha.R
import com.turkcell.turkcellsaha.data.model.SummaryContent
import com.turkcell.turkcellsaha.databinding.ItemCategoryRvBinding


class CategoryRvAdapter : RecyclerView.Adapter<CategoryRvAdapter.ViewHolder>() {

    private val itemList = mutableListOf<SummaryContent>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemCategoryRvBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_category_rv, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun appendList(list: List<SummaryContent>) {
        itemList.addAll(list)
        notifyDataSetChanged()
    }

    class ViewHolder(private val itemRowBinding: ItemCategoryRvBinding) :
        RecyclerView.ViewHolder(itemRowBinding.root) {

        fun bind(content: SummaryContent) {
            itemRowBinding.setVariable(BR.viewData, content)
            itemRowBinding.executePendingBindings()

        }

    }
}