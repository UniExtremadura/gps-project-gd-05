package com.gd05.brickr.ui.search

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gd05.brickr.R
import com.gd05.brickr.databinding.FavoriteItemListBinding
import com.gd05.brickr.databinding.SearchItemListBinding
import com.gd05.brickr.model.BrickSet


class FavoriteAdapter(
    private var sets: List<BrickSet>,
    private val onClick: (set: BrickSet) -> Unit,
    private val onLongClick: (set: BrickSet) -> Unit,
    private val context: Context?
) : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {
    class FavoriteViewHolder(
        private val binding: FavoriteItemListBinding,
        private val onClick: (set: BrickSet) -> Unit,
        private val onLongClick: (title: BrickSet) -> Unit,
        private val context: Context?,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(set: BrickSet, totalItems: Int) {
            with(binding) {
                itemName.text = set.name

                context?.let {
                    Glide.with(context)
                        .load(set.setImgUrl)
                        .placeholder(R.drawable.brick_placeholder)
                        .into(itemImg)
                }

                cvItem.setOnClickListener {
                    onClick(set)
                }
                cvItem.setOnLongClickListener {
                    onLongClick(set)
                    true
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = FavoriteItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHolder(binding, onClick, onLongClick, context)
    }

    override fun getItemCount() = sets.size

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(sets[position], sets.size)
    }

    fun updateData(newSets: List<BrickSet>) {
        sets = newSets
        notifyDataSetChanged()
    }
}