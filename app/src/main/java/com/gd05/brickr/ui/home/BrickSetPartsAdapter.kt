package com.gd05.brickr.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gd05.brickr.R
import com.gd05.brickr.databinding.BricksetPartsItemListBinding
import com.gd05.brickr.model.Brick

class BrickSetPartsAdapter(

    private var bricks: List<Brick>,
    private var amounts: Map<String, Int>,
    private val onClick: (brick: Brick) -> Unit,
    private val onLongClick: (title: Brick) -> Unit,
    private val context: Context?,
    private var localAmount: Map<String, Int>
) : RecyclerView.Adapter<BrickSetPartsAdapter.BrickSetPartViewHolder>() {

    class BrickSetPartViewHolder(
        private val binding: BricksetPartsItemListBinding,
        private val onClick: (brick: Brick) -> Unit,
        private val onLongClick: (title: Brick) -> Unit,
        private val context: Context?,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(brick: Brick, amounts: Map<String, Int>, localAmounts: Map<String, Int>) {
            with(binding) {
                bricksetPartTitle.text = brick.name
                bricksetPartId.text = "#${brick.brickId}"
                /* TODO: Add needed bricks and owned bricks */
                bricksetPartAmount.text =
                    "${localAmounts[brick.brickId]} / ${amounts[brick.brickId]}"

                /* Add images to bricks */
                context?.let {
                    Glide.with(context)
                        .load(brick.brickImgUrl)
                        .placeholder(R.drawable.brick_placeholder)
                        .into(itemImg)
                }

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrickSetPartViewHolder {
        val binding =
            BricksetPartsItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BrickSetPartViewHolder(
            binding,
            onClick,
            onLongClick,
            context
        )
    }

    override fun getItemCount() = bricks.size

    override fun onBindViewHolder(holder: BrickSetPartViewHolder, position: Int) {
        holder.bind(bricks[position], amounts, localAmount)
    }

    fun updateData(
        newBricks: List<Brick>,
        newAmount: Map<String, Int>,
        localAmount: Map<String, Int>
    ) {
        this.amounts = newAmount
        this.bricks = newBricks
        this.localAmount = localAmount
        notifyDataSetChanged()
    }
}