package com.gd05.brickr.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.gd05.brickr.R
import com.gd05.brickr.database.BrickrDatabase
import com.gd05.brickr.databinding.FragmentBricksetDetailBinding
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [BrickSetDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BrickSetDetailFragment : Fragment() {

    private var _binding: FragmentBricksetDetailBinding? = null
    private val binding get() = _binding!!
    private  lateinit var db: BrickrDatabase

    private val args: BrickSetDetailFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBricksetDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun getTheme(themeId: Int): String {
        var categoryName: String = ""

        lifecycleScope.launch {
            db = BrickrDatabase.getInstance(requireContext())!!
            val theme = db.themeDao().getThemeById(themeId)
            var tName = theme?.themeName ?: "Unknown"
            binding.brickSetDetailsTheme.text = tName
        }

        return categoryName
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val brickSet = args.brickSet
        binding.brickSetDetailsName.text = brickSet.name
        binding.brickSetDetailsYearReleased.text = brickSet.year.toString()
        binding.brickSetDetailsId.text = "#${brickSet.brickSetId.toString()}"
        binding.brickSetDetailsIdText.text = "#${brickSet.brickSetId.toString()}"
        binding.brickSetDetailsTheme.text = brickSet.themeId?.let { getTheme(it) }
        binding.brickSetDetailsNumPartsText.text = brickSet.numParts.toString()
        binding.shareSetButton.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "Hey! Check out this set: ${brickSet.name}!\n\n${brickSet.setUrl}")
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }


        context?.let {
            Glide.with(requireContext())
                .load(brickSet.setImgUrl)
                .placeholder(R.drawable.brick_placeholder)
                .into(binding.coverImg)
        }


        /*
        binding.brickDetailsRemove.setOnClickListener {
            lifecycleScope.launch {

            }
        }

        binding.brickDetailsDestroy.setOnClickListener {
            lifecycleScope.launch {

            }
        }*/

    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         * @return A new instance of fragment BrickDetailFragment.
         */
        @JvmStatic
        fun newInstance() =
            BrickSetDetailFragment()
    }
}