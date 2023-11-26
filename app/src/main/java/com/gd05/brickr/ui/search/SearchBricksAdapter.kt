package com.gd05.brickr.ui.search

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gd05.brickr.R
import com.gd05.brickr.databinding.SearchItemListBinding
import com.gd05.brickr.model.Brick


class SearchBricksAdapter(
    private var bricks: List<Brick>,
    private val onClick: (brick: Brick) -> Unit,
    private val onLongClick: (title: Brick) -> Unit,
    private val context: Context?
) : RecyclerView.Adapter<SearchBricksAdapter.BrickViewHolder>() {
    class BrickViewHolder(
        private val binding: SearchItemListBinding,
        private val onClick: (brick: Brick) -> Unit,
        private val onLongClick: (title: Brick) -> Unit,
        private val context: Context?,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(brick: Brick, totalItems: Int) {
            with(binding) {
                searchItemName.text = brick.name

                // --------------- TODO: Change this to already downloaded image ---------------
                context?.let {
                    Glide.with(context)
                        .load(brick.brickImgUrl)
                        .placeholder(R.drawable.brick_placeholder)
                        .into(searchItemImg)
                }
                // -----------------------------------------------------------------------------


                cvItem.setOnClickListener {
                    onClick(brick)
                }
                cvItem.setOnLongClickListener {
                    onLongClick(brick)
                    true
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrickViewHolder {
        val binding =
            SearchItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BrickViewHolder(binding, onClick, onLongClick, context)
    }

    override fun getItemCount() = bricks.size

    override fun onBindViewHolder(holder: BrickViewHolder, position: Int) {
        holder.bind(bricks[position], bricks.size)
    }

    //TODO metodo para actualizar los datos del adapter
    fun updateData(newBricks: List<Brick>) {
        bricks = newBricks
        notifyDataSetChanged()
    }
}