package com.gd05.brickr.ui.home

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gd05.brickr.R
import com.gd05.brickr.databinding.InventoryItemListBinding
import com.gd05.brickr.model.Brick


class InventoryAdapter(
    private var bricks: List<Brick>,
    private val onClick: (brick: Brick) -> Unit,
    private val onLongClick: (title: Brick) -> Unit,
    private val onRemoveClick: (brick: Brick) -> Unit,
    private val onAddClick: (brick: Brick) -> Unit,
    private val onDestroyClick: (brick: Brick) -> Unit,
    private val context: Context?
) : RecyclerView.Adapter<InventoryAdapter.BrickViewHolder>() {
    class BrickViewHolder(
        private val binding: InventoryItemListBinding,
        private val onClick: (brick: Brick) -> Unit,
        private val onLongClick: (title: Brick) -> Unit,
        private val onAddClick: (brick: Brick) -> Unit,
        private val onRemoveClick: (brick: Brick) -> Unit,
        private val onDestroyClick: (brick: Brick) -> Unit,
        private val context: Context?,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(brick: Brick, totalItems: Int) {
            with(binding) {
                inventoryTitle.text = brick.name
                inventoryId.text = "#${brick.brickId.toString()}"
                inventoryAmount.text = brick.amount.toString();

                // --------------- TODO: Change this to already downloaded image ---------------
                context?.let {
                    Glide.with(context)
                        .load(brick.brickImgUrl)
                        .placeholder(R.drawable.dummy_brick_bright_light_orange)
                        .into(itemImg)
                }
                // -----------------------------------------------------------------------------

                cvItem.setOnClickListener {
                    onClick(brick)
                }
                cvItem.setOnLongClickListener {
                    onLongClick(brick)
                    true
                }

                inventoryAdd.setOnClickListener {
                    onAddClick(brick)
                }

                inventoryRemove.setOnClickListener {
                    onRemoveClick(brick)
                }

            //  inventoryDestroy.setOnClickListener {
            //     onDestroyClick(brick)
            //}

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrickViewHolder {
        val binding =
            InventoryItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BrickViewHolder(binding, onClick, onLongClick, onAddClick, onRemoveClick, onDestroyClick, context)
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