package com.gd05.brickr.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.gd05.brickr.BrickrApplication
import com.gd05.brickr.R
import com.gd05.brickr.database.Repository
import com.gd05.brickr.databinding.FragmentBrickDetailBinding
import com.gd05.brickr.model.Brick
import com.gd05.brickr.model.BrickSet
import kotlinx.coroutines.launch

private const val TAG = "BrickDetailFragment"

/**
 * A simple [Fragment] subclass.
 * Use the [BrickDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BrickDetailFragment : Fragment() {

    private var _binding: FragmentBrickDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: BrickDetailViewModel by viewModels { BrickDetailViewModel.Factory }

    private val args: BrickDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBrickDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val brick = args.brick
        viewModel.brick = brick
        subscribeBrickDetailUi()
    }

    private fun brickBinding(brick: Brick){
        binding.brickDetailsName.text = brick.name
        binding.brickDetailsYearFrom.text = brick.yearFrom.toString()
        binding.brickDetailsYearFromText.text = brick.yearFrom.toString()
        binding.brickDetailsYearTo.text = brick.yearTo.toString()
        binding.brickDetailsYearToText.text = brick.yearTo.toString()
        binding.brickDetailsId.text = "#${brick.brickId.toString()}"
        binding.brickDetailsIdText.text = "#${brick.brickId.toString()}"
        binding.brickDetailsAmount.text = brick.amount.toString()

        //Nombre de la categoria
        viewModel.getBrickCategory(brick.categoryId ?: 0).observe(viewLifecycleOwner) { categoryName ->
            binding.brickDetailsCategory.text = categoryName
        }


        binding.brickDetailsRebrickableButton.setOnClickListener {
            val urlIntent = Intent(Intent.ACTION_VIEW, Uri.parse(brick.brickUrl))
            startActivity(urlIntent)
            Toast.makeText(context, "Opening Rebrickable...", Toast.LENGTH_SHORT).show()
        }

        context?.let {
            Glide.with(requireContext())
                .load(brick.brickImgUrl)
                .placeholder(R.drawable.brick_placeholder)
                .into(binding.coverImg)
        }

        binding.brickDetailsAdd.setOnClickListener {
            viewModel.addBrick(brick)
        }

        binding.brickDetailsRemove.setOnClickListener {
            viewModel.removeBrick(brick)
        }

        binding.brickDetailsDestroy.setOnClickListener {
            viewModel.destroyBrick(brick)
        }
    }

    private fun subscribeBrickDetailUi() {
        viewModel.brickDetail.observe(viewLifecycleOwner) { brick ->
            brick?.let { brickBinding(brick) }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         * @return A new instance of fragment BrickDetailFragment.
         */
        @JvmStatic
        fun newInstance() =
            BrickDetailFragment()
    }
}