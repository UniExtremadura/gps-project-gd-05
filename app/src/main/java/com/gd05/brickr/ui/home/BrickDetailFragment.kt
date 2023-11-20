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
import androidx.navigation.fragment.navArgs
import com.gd05.brickr.R
import com.gd05.brickr.databinding.FragmentBrickDetailBinding

private const val TAG = "BrickDetailFragment"

/**
 * A simple [Fragment] subclass.
 * Use the [BrickDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BrickDetailFragment : Fragment() {

    private var _binding: FragmentBrickDetailBinding? = null
    private val binding get() = _binding!!

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
        binding.brickDetailsName.text = brick.name
        binding.brickDetailsYearFrom.text = brick.yearFrom.toString()
        binding.brickDetailsYearFromText.text = brick.yearFrom.toString()
        binding.brickDetailsYearTo.text = brick.yearTo.toString()
        binding.brickDetailsYearToText.text = brick.yearTo.toString()
        binding.brickDetailsId.text = "#${brick.brickId.toString()}"
        binding.brickDetailsIdText.text = "#${brick.brickId.toString()}"
        binding.brickDetailsAmount.text = brick.amount.toString()
        binding.brickDetailsRebrickableButton.setOnClickListener {
            val urlIntent = Intent(Intent.ACTION_VIEW, Uri.parse(brick.brickUrl))
            startActivity(urlIntent)
            Toast.makeText(context, "Opening Rebrickable...", Toast.LENGTH_SHORT).show()
        }


        // TODO: Load correct image
        binding.coverImg.setImageResource(R.drawable.dummy_brick_bright_light_orange)
        Log.d(TAG, "Showing ${brick.name} details")
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