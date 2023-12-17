package com.gd05.brickr.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.gd05.brickr.BrickrApplication
import com.gd05.brickr.R
import com.gd05.brickr.database.Repository
import com.gd05.brickr.databinding.FragmentBricksetDetailBinding
import com.gd05.brickr.model.BrickSet
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [BrickSetDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BrickSetDetailFragment : Fragment() {

    private var _binding: FragmentBricksetDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: BrickSetDetailViewModel by viewModels { BrickSetDetailViewModel.Factory }

    private val args: BrickSetDetailFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBricksetDetailBinding.inflate(inflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val brickSet = args.brickSet
        viewModel.brickSet = brickSet
        viewModel.toast.observe(viewLifecycleOwner) { text ->
            text?.let {
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
                viewModel.onToastShown()
            }
        }
        subscribeBrickSetDetailUi()
    }

    private fun brickSetBinding(brickSet: BrickSet){
        binding.brickSetDetailsName.text = brickSet.name
        binding.brickSetDetailsYearReleased.text = brickSet.year.toString()
        binding.brickSetDetailsId.text = "#${brickSet.brickSetId.toString()}"
        binding.brickSetDetailsIdText.text = "#${brickSet.brickSetId.toString()}"
        binding.brickSetDetailsNumPartsText.text = brickSet.numParts.toString()

        //Imagen del set
        context?.let {
            Glide.with(requireContext())
                .load(brickSet.setImgUrl)
                .placeholder(R.drawable.brick_placeholder)
                .into(binding.coverImg)
        }

        //Nombre del Theme
        viewModel.getBrickSetTheme(brickSet.themeId ?: 0).observe(viewLifecycleOwner) { themeName ->
            binding.brickSetDetailsTheme.text = themeName
        }
        //Boton de compartir set
        binding.shareSetButton.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "Hey! Check out this set: ${brickSet.name}!\n\n${brickSet.setUrl}")
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }


        // Boton piezas de un set
        binding.puzzleButton.setOnClickListener {
            val action = BrickSetDetailFragmentDirections.actionBrickDetailSetDetailFragmentToBrickSetPartsFragment(brickSet)
            view?.findNavController()?.navigate(action)
        }

        //Boton de favorito
        binding.toggleFavorite.isChecked = viewModel.brickSet?.isFavorite ?: false
        binding.toggleFavorite.setOnClickListener {
            viewModel.toggleFavorite(brickSet)
        }

    }


    private fun subscribeBrickSetDetailUi() {
        viewModel.brickSetDetail.observe(viewLifecycleOwner) { brickset ->
            brickset?.let { brickSetBinding(brickset) }
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