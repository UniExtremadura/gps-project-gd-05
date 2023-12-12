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
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.gd05.brickr.R
import com.gd05.brickr.api.RebrickableService
import com.gd05.brickr.database.BrickrDatabase
import com.gd05.brickr.database.Repository
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
    private lateinit var db: BrickrDatabase
    private lateinit var repository: Repository

    private val args: BrickSetDetailFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        db = BrickrDatabase.getInstance(requireContext())!!
        repository = Repository.getInstance(
            db.brickDao(),
            db.brickSetDao(),
            db.categoryDao(),
            db.themeDao(),
            RebrickableService
        )
        _binding = FragmentBricksetDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun getTheme(themeId: Int): String {
        var themeName: String = ""

        lifecycleScope.launch {
            val theme = repository.publicGetThemeName(themeId)
            themeName = theme?.themeName ?: "Unknown"
            binding.brickSetDetailsTheme.text = themeName
        }

        return themeName
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

        binding.toggleFavorite.isChecked = brickSet.isFavorite

        /* Navigate to BrickSetPartsFragment when the user clicks on the "View Parts" button */
        binding.puzzleButton.setOnClickListener {
            val action = BrickSetDetailFragmentDirections.actionBrickDetailSetDetailFragmentToBrickSetPartsFragment(brickSet)

            view.findNavController().navigate(action)
        }

        // Set an OnClickListener to handle ToggleButton state changes
        binding.toggleFavorite.setOnClickListener {
            // Toggle the isFavorite state when the button is clicked
            brickSet.isFavorite = !brickSet.isFavorite

            // Update the UI to reflect the new state
            binding.toggleFavorite.isChecked = brickSet.isFavorite


            // You can also perform any additional actions based on the new state here
            if (brickSet.isFavorite) {

                // Do something when the item is marked as favorite
                lifecycleScope.launch {
                    brickSet.isFavorite = true

                    if(repository.publicGetThemeName(brickSet.themeId!!)  != null){
                        repository.publicInsertBrickSet(brickSet)

                        Toast.makeText(requireContext(), "Marcado como favorito", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(requireContext(), "El set no se encuentra disponible", Toast.LENGTH_SHORT).show()
                    }

                }
            }else {
                // Do something when the item is unmarked as favorite
                lifecycleScope.launch {
                    brickSet.isFavorite = false
                    if(repository.publicGetThemeName(brickSet.themeId!!) != null){
                        brickSet.isFavorite = false
                        repository.publicInsertBrickSet(brickSet)
                        Toast.makeText(requireContext(), "Desmarcado de favorito", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(requireContext(), "El set no se encuentra disponible", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }


        context?.let {
            Glide.with(requireContext())
                .load(brickSet.setImgUrl)
                .placeholder(R.drawable.brick_placeholder)
                .into(binding.coverImg)
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
            BrickSetDetailFragment()
    }
}