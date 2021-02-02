package com.kafein.turkcellsaha.ui.home.adapter

import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kafein.turkcellsaha.BR
import com.kafein.turkcellsaha.R
import com.kafein.turkcellsaha.data.model.WallContent
import com.kafein.turkcellsaha.databinding.ItemWallContentRvBinding

typealias OnContentClickListener = (WallContent) -> Unit
typealias OnMoreClickListener = (WallContent) -> Unit

class WallContentRvAdapter : RecyclerView.Adapter<WallContentRvAdapter.ViewHolder>() {

    private val itemList = mutableListOf<WallContent>()
    var onItemClickListener: OnContentClickListener? = null
    var onMoreClickListener: OnMoreClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemWallContentRvBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_wall_content_rv, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
        holder.itemView.findViewById<CardView>(R.id.cardPost).setOnClickListener {
            onItemClickListener?.invoke(itemList[position])
        }

        holder.itemView.findViewById<AppCompatImageView>(R.id.ivMore).setOnClickListener {
            onMoreClickListener?.invoke(itemList[position])
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun appendList(list: List<WallContent>) {
        itemList.addAll(list)
        notifyDataSetChanged()
    }

    class ViewHolder(private val itemRowBinding: ItemWallContentRvBinding) :
        RecyclerView.ViewHolder(itemRowBinding.root) {

        fun bind(content: WallContent) {
            if (content.preview.isNullOrEmpty()) {
                Glide.with(itemRowBinding.ivContent.context).load(R.drawable.placeholder)
                    .into(itemRowBinding.ivContent)
            } else {
                Glide.with(itemRowBinding.ivContent.context).asBitmap().load(Base64.decode(content.preview, Base64.DEFAULT))
                    .placeholder(R.drawable.placeholder).into(itemRowBinding.ivContent)
            }

            itemRowBinding.setVariable(BR.viewData, content)
            itemRowBinding.executePendingBindings()

        }

    }
}