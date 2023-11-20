package com.gd05.brickr.ui.home

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gd05.brickr.R
import com.gd05.brickr.databinding.InventoryItemListBinding
import com.gd05.brickr.model.Brick


class InventoryAdapter(
    private var bricks: List<Brick>,
    private val onClick: (brick: Brick) -> Unit,
    private val onLongClick: (title: Brick) -> Unit,
    private val context: Context?
) : RecyclerView.Adapter<InventoryAdapter.BrickViewHolder>() {
    class BrickViewHolder(
        private val binding: InventoryItemListBinding,
        private val onClick: (brick: Brick) -> Unit,
        private val onLongClick: (title: Brick) -> Unit,

    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(brick: Brick, totalItems: Int) {
            with(binding) {
                inventoryTitle.text = brick.name
                inventoryId.text = "#${brick.brickId.toString()}"
                inventoryAmount.text = brick.amount.toString();

                // --------------- TODO: Change this to already downloaded image ---------------
                itemImg.setImageResource(R.drawable.dummy_brick_bright_light_orange)
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
            InventoryItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BrickViewHolder(binding, onClick, onLongClick)
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