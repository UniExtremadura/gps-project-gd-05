package com.gd05.brickr.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.gd05.brickr.R
import com.gd05.brickr.database.BrickrDatabase
import com.gd05.brickr.databinding.FragmentBrickDetailBinding
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
    private  lateinit var db: BrickrDatabase

    private val args: BrickDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBrickDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun getCategory(categoryId: Int): String {
        var categoryName: String = ""

        lifecycleScope.launch {
            db = BrickrDatabase.getInstance(requireContext())!!
            val category = db.categoryDao().getCategoryById(categoryId)
            categoryName = category?.categoryName ?: "Unknown"
            binding.brickDetailsCategory.text = categoryName
        }

        return categoryName
    }

    private fun getInventoryAmount(brickId: String ): Int {
        var brickAmount: Int = 0

        lifecycleScope.launch {
            db = BrickrDatabase.getInstance(requireContext())!!
            val brick = db.brickDao().findById(brickId)
            brickAmount = brick?.amount ?: 0
            binding.brickDetailsAmount.text = brickAmount.toString()
        }

        return brickAmount
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val brick = args.brick
        binding.brickDetailsName.text = brick.name
        binding.brickDetailsYearFrom.text = brick.yearFrom.toString()
        binding.brickDetailsYearFromText.text = brick.yearFrom.toString()
        binding.brickDetailsYearTo.text = brick.yearTo.toString()
        binding.brickDetailsYearToText.text = brick.yearTo.toString()
        binding.brickDetailsId.text = "#${brick.brickId.toString()}"
        binding.brickDetailsIdText.text = "#${brick.brickId.toString()}"
        getInventoryAmount(brick.brickId)
        binding.brickDetailsCategory.text = brick.categoryId?.let { getCategory(it) }
        binding.brickDetailsRebrickableButton.setOnClickListener {
            val urlIntent = Intent(Intent.ACTION_VIEW, Uri.parse(brick.brickUrl))
            startActivity(urlIntent)
            Toast.makeText(context, "Opening Rebrickable...", Toast.LENGTH_SHORT).show()
        }


        // TODO: Load correct image
        context?.let {
            Glide.with(requireContext())
                .load(brick.brickImgUrl)
                .placeholder(R.drawable.brick_placeholder)
                .into(binding.coverImg)
        }

        binding.brickDetailsAdd.setOnClickListener {
            lifecycleScope.launch {
                brick.amount++
                db.brickDao().insert(brick)
                binding.brickDetailsAmount.text = brick.amount.toString()

            }
        }

        binding.brickDetailsRemove.setOnClickListener {
            lifecycleScope.launch {
                if (brick.amount > 1) {
                    brick.amount--
                    db.brickDao().insert(brick)
                    binding.brickDetailsAmount.text = brick.amount.toString()
                }
            }
        }

        binding.brickDetailsDestroy.setOnClickListener {
            lifecycleScope.launch {
                db.brickDao().delete(brick)
                brick.amount = 0
                binding.brickDetailsAmount.text = brick.amount.toString()
            }
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