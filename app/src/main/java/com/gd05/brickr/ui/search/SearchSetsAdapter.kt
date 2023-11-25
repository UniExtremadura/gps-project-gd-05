package com.gd05.brickr.ui.search

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gd05.brickr.R
import com.gd05.brickr.databinding.SearchItemListBinding
import com.gd05.brickr.model.Brick
import com.gd05.brickr.model.BrickSet


class SearchSetsAdapter(
    private var sets: List<BrickSet>,
    private val onClick: (set: BrickSet) -> Unit,
    private val onLongClick: (set: BrickSet) -> Unit,
    private val context: Context?
) : RecyclerView.Adapter<SearchSetsAdapter.SetsViewHolder>() {
    class SetsViewHolder(
        private val binding: SearchItemListBinding,
        private val onClick: (set: BrickSet) -> Unit,
        private val onLongClick: (title: BrickSet) -> Unit,
        private val context: Context?,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(set: BrickSet, totalItems: Int) {
            with(binding) {
                searchItemName.text = set.name

                context?.let {
                    Glide.with(context)
                        .load(set.setImgUrl)
                        .placeholder(R.drawable.brick_placeholder)
                        .into(searchItemImg)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SetsViewHolder {
        val binding = SearchItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SetsViewHolder(binding, onClick, onLongClick, context)
    }

    override fun getItemCount() = sets.size

    override fun onBindViewHolder(holder: SetsViewHolder, position: Int) {
        holder.bind(sets[position], sets.size)
    }

    fun updateData(newSets: List<BrickSet>) {
        sets = newSets
        notifyDataSetChanged()
    }
}